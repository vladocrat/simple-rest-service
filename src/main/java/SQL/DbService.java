package SQL;

import Dto.PersonDto;
import Dao.PersonDao;
import Utils.YamlUtils;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class DbService implements CRUDRepository {
    private Connection conn = null;

    @Override
    public void openConnection() throws IOException {
        DbConfig config = YamlUtils.getSqlConfig();
        String url = config.getUrl();
        String password = config.getPassword();
        String username = config.getUsername();

        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("connection to db successful");

            if(tableExists(conn, "PEOPLE")) {
                System.out.println("exists and connected");
            } else {
                String query = "CREATE TABLE PEOPLE" +
                        "(id INTEGER not NULL AUTO_INCREMENT, " +
                        " fio VARCHAR(255), " +
                        " birth_date DATE, " +
                        " PRIMARY KEY ( id ))";
                Statement statement = conn.createStatement();
                statement.executeUpdate(query);
                if(tableExists(conn, "PEOPLE")) {
                    System.out.println("table has successfully been created and connected to");
                } else {
                    System.out.println("ERROR::table wasn't created");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PersonDao> findAll() {
        List<PersonDao> result = new ArrayList<>();

        checkConnection();

        String query = "SELECT id, fio, birth_date from people";
        try(Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String fio = resultSet.getString("fio");
                Date date = resultSet.getDate("birth_date");
                result.add(new PersonDao(id, fio, date.toLocalDate()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public PersonDao findPersonById(int id)  {

        checkConnection();
        PersonDao dao = null;
        try {
            String query = "SELECT * FROM PEOPLE where id = " + id;
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            String fio = "";
            LocalDate birth_date = null;
            while(resultSet.next()) {
                 fio = resultSet.getString("fio");
                 birth_date = resultSet.getDate("birth_date").toLocalDate();
            }

             dao = new PersonDao(id, fio, birth_date);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return dao;
    }

    @Override
    public boolean save(PersonDto dto)  {
        if(dto == null) {
            System.out.println("dto is null");
            return false;
        }

        PersonDao dao = new PersonDao(dto.getSurname(),
                dto.getName(),
                dto.getSecondName(),
                dto.getBirthDate());
        String fio = dao.getFio();
        LocalDate birthDate = dao.getBirthDate();
        Date date = Date.valueOf(birthDate);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        checkConnection();

        String query = "INSERT INTO PEOPLE(fio, birth_date) VALUES(?, ?)";

        try(PreparedStatement statement = conn.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, fio);
            statement.setDate(2, sqlDate);

            int affectedRows = statement.executeUpdate();

            if (affectedRows ==  0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dao.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }


    @Override
    public boolean delete(int id) throws SQLException {
        checkConnection();

        PersonDao person = findPersonById(id);
        if(person.getFio().equals("") && person.getBirthDate() == null) return false;

        if(person.getId() == 0) return false;

        String query = "DELETE FROM PEOPLE WHERE id=" + id;
        Statement statement = conn.createStatement();
        statement.executeUpdate(query);


        return true;
    }

    @Override
    public boolean update(Map<String, String> params) throws SQLException {
        checkConnection();
        String id = params.get("id");
        PersonDao dao = findPersonById(Integer.parseInt(id));

        if(dao == null) return false;

        String fio = getFio(params, dao);
        Date sqlDate = getSqlDate(params, dao);
        String date = sqlDate.toString();

        String query = "UPDATE PEOPLE" +
                " SET" +
                " fio = '" + fio + "'," +
                " birth_date = DATE_FORMAT('" + date +"', '%y-%m-%d')" +
                " WHERE id = " + id + ";";
        System.out.println(query);
        Statement statement = conn.createStatement();
        statement.executeUpdate(query);

        return true;
    }



    private String getFio(Map<String, String> params, PersonDao dao) {
        String fio = "";
        String surname = params.get("surname");
        String name = params.get("name");
        String secondName = params.get("secondname");

        PersonDto dto = dao.convertFioAndCreatePerson();

        if(surname.equals("default")) {
            fio += dto.getSurname() + " ";
        } else {
            fio += surname + " ";
        }
        if(name.equals("default")) {
            fio += dto.getName() + " ";
        } else {
            fio += name + " ";
        }
        if(secondName.equals("default")) {
            fio += dto.getSecondName() + " ";
        } else {
            fio += secondName;
        }
        return fio;
    }

    private Date getSqlDate(Map<String, String> params, PersonDao dao) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateFromMap = LocalDate.parse(params.get("date"), formatter);
        String date = dateFromMap.toString();

        Date newDate;
        if("default".equals(date)) {
            newDate = Date.valueOf(dao.getBirthDate());
        } else {
            newDate = Date.valueOf(dateFromMap);
        }
        return newDate;
    }


    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"});
        return resultSet.next();
    }

    private void checkConnection() {
        if(conn == null) {
            try {
                openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeConnection() {
        try {
            if(conn != null) {
                System.out.println("closing connection to db...");
                conn.close();
                System.out.println("connection has successfully been closed");
            }
        } catch (SQLException e) {
            System.out.println("ERROR::failed to close connection to db");
            e.printStackTrace();
        }
    }

}
