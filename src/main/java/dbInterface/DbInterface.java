package dbInterface;

public interface DbInterface<T> {

    void initDB(String uri);

    void createData(T data);

    T readData(String id);

    void updateData(String id,T data);

    void deleteData(String id);

    void close();
}
