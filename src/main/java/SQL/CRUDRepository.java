package SQL;

import Dto.PersonDto;
import Dao.PersonDao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CRUDRepository {
    void openConnection() throws IOException;
    List<PersonDao> findAll();
    PersonDao findPersonById(int id);
    boolean save(PersonDto personDto) throws SQLException;
    boolean delete(int id) throws SQLException;
    boolean update(Map<String, String> param) throws  SQLException;
}
