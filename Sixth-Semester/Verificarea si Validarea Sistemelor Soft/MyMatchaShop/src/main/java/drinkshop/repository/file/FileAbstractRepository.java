package drinkshop.repository.file;

import drinkshop.repository.AbstractRepository;
import drinkshop.exception.RepositoryException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public abstract class FileAbstractRepository<ID, E> extends AbstractRepository<ID, E> {

    protected String fileName;

    protected FileAbstractRepository(String fileName) {
        this.fileName = fileName;
    }

    protected void loadFromFile() {
        entities.clear();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                E entity = extractEntity(line);
                super.save(entity);
            }
        } catch (IOException exception) {
            throw new RepositoryException("Nu s-a putut citi fisierul: " + fileName, exception);
        }
    }

    private void writeToFile() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            for (E entity : entities.values()) {
                bufferedWriter.write(createEntityAsString(entity));
                bufferedWriter.newLine();
            }
        } catch (IOException exception) {
            throw new RepositoryException("Nu s-a putut scrie in fisierul: " + fileName, exception);
        }
    }

    @Override
    public E save(E entity) {
        E savedEntity = super.save(entity);
        writeToFile();
        return savedEntity;
    }

    @Override
    public E delete(ID id) {
        E deletedEntity = super.delete(id);
        writeToFile();
        return deletedEntity;
    }

    @Override
    public E update(E entity) {
        E updatedEntity = super.update(entity);
        writeToFile();
        return updatedEntity;
    }

    protected abstract E extractEntity(String line);

    protected abstract String createEntityAsString(E entity);
}
