package tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class WebTemplate {

    private final String DIRECTORY_TEMPLATES = "web/html/template";

    private String filePath;
    private Map<String, String> values;

    public WebTemplate(String filePath, Map<String, String> values) {
        this.filePath = filePath;
        this.values = values;
    }

    public String getFilePath() {
        return filePath;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public String generateContent(ClassLoader loader) {
        String templateContent = readTemplateFile(loader);
        for (Map.Entry<String, String> entry : values.entrySet())
            templateContent = templateContent.replace("{" + entry.getKey() + "}", entry.getValue());
        return templateContent;
    }

    private String readTemplateFile(ClassLoader loader) {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedInputStream bin = new BufferedInputStream(
                    loader.getResourceAsStream(DIRECTORY_TEMPLATES + File.separator + filePath));
            byte[] contents = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = bin.read(contents)) != -1) builder.append(new String(contents, 0, bytesRead));
            bin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
