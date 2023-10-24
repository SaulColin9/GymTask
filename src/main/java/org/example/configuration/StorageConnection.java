package org.example.configuration;

import java.io.IOException;
import java.util.List;

public interface StorageConnection<T> {
    T getEntities(String filePath) throws IOException;
    List<T> writeEntities(String filePath, List<T> entities) throws IOException;
}