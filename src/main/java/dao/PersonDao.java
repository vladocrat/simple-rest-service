package dao;

import dto.PersonDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class PersonDao {
    private int id;
    private String fio;
    private LocalDate birthDate;

    public PersonDao(String surname, String name, String secondName, LocalDate date) {
        fio = surname + " " + name + " " + secondName;
        birthDate = date;
    }

    public PersonDto convertFioAndCreatePerson() {
        String[] arr = fio.split(" ");
        return new PersonDto(arr[0], arr[1], arr[2], birthDate);
    }

    @Override
    public String toString() {
        return "Person {" +
                "id=" + id +
                ", fio='" + fio + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
