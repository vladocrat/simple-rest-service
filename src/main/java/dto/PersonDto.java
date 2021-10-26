package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class PersonDto {
    private String surname;
    private String name;
    private String secondName;
    private LocalDate birthDate;

    @Override
    public String toString() {
        return "Person {" +
                "surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
