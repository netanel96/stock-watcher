package dbInterface;

import java.util.List;

public interface DbInterface<T> {

    void initDB(String uri);

    void createData(T data);

    T readData(String id);

    List<T> readListOfData(T data);

    List<T> readListOfData();

    void updateData(String id,T data);

    void deleteData(String id);

    void close();
}
