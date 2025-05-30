package ru.top.smartcity.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import ru.top.smartcity.models.Category;
import ru.top.smartcity.utils.CombinedData;

import java.io.*;
import java.util.*;

@Service
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final String dataFilePath;
    private final ResourceLoader resourceLoader;
    private Map<String, List<String>> categories = new HashMap<>();

    @Autowired
    public CategoryService(@Value("${app.data.file:classpath:/data/data.json}") String dataFilePath, ResourceLoader resourceLoader) {
        this.dataFilePath = dataFilePath;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() {
        try {
            Resource resource = resourceLoader.getResource("classpath:data/data.json");
            if (!resource.exists()) {
                throw new FileNotFoundException("Data file not found");
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(resource.getInputStream());
            JsonNode categoriesNode = rootNode.path("categories");

            if (categoriesNode.isMissingNode() || categoriesNode.isEmpty()) {
                throw new IllegalStateException("No valid 'categories' field in data file");
            }

            this.categories = mapper.convertValue(
                    categoriesNode,
                    new TypeReference<Map<String, List<String>>>() {
                    }
            );

            log.info("Categories loaded successfully: {}", categories.keySet());
        } catch (Exception e) {
            log.error("Failed to load categories. Using defaults.", e);
            initDefaultCategories();
        }
    }

    private void initDefaultCategories() {
        this.categories = new HashMap<>();
        this.categories.put("city", new ArrayList<>(List.of(
                "Парки", "Музеи и галереи", "Набережные", "Архитектура", "Пляжи"
        )));
        this.categories.put("surrounding", new ArrayList<>(List.of(
                "Горы", "Сооружения", "Другие интересные места"
        )));
    }

    private void loadCategories(Resource resource) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = resource.getInputStream()) {
            JsonNode rootNode = mapper.readTree(inputStream);
            JsonNode categoriesNode = rootNode.path("categories");

            if (categoriesNode.isMissingNode()) {
                throw new IllegalStateException("No 'categories' field in data file");
            }

            this.categories = mapper.convertValue(
                    categoriesNode,
                    new TypeReference<Map<String, List<String>>>() {
                    }
            );
        }
    }


    @Cacheable("cityCategories")
    public List<String> getCityCategories() {
        return new ArrayList<>(categories.getOrDefault("city", Collections.emptyList()));
    }

    @Cacheable("surroundingCategories")
    public List<String> getSurroundingCategories() {
        return new ArrayList<>(categories.getOrDefault("surrounding", Collections.emptyList()));
    }

    public List<Category> getAllCategories() {
        List<Category> allCategories = new ArrayList<>();

        categories.getOrDefault("city", Collections.emptyList())
                .forEach(name -> allCategories.add(new Category(name, "city")));

        categories.getOrDefault("surrounding", Collections.emptyList())
                .forEach(name -> allCategories.add(new Category(name, "surrounding")));

        return allCategories;
    }

    @CacheEvict(allEntries = true)
    public void updateCategories(List<Category> updatedCategories) throws IOException {
        Map<String, List<String>> newCategories = new HashMap<>();
        newCategories.put("city", new ArrayList<>());
        newCategories.put("surrounding", new ArrayList<>());

        updatedCategories.forEach(c -> {
            if (c.getName() != null && !c.getName().trim().isEmpty()) {
                newCategories.get(c.getType()).add(c.getName().trim());
            }
        });

        this.categories = newCategories;
        saveCategories();
    }

    private void saveCategories() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Resource resource = resourceLoader.getResource(dataFilePath);

        CombinedData data;
        try (InputStream inputStream = resource.getInputStream()) {
            data = mapper.readValue(inputStream, CombinedData.class);
        }

        data.setCategories(categories);

        try (OutputStream outputStream = new FileOutputStream(resource.getFile())) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, data);
        }
    }

}
