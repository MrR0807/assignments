package lt.laurynas.homework.authentication.rest.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;

import java.util.Objects;

@ApiModel
public class PasswordHash {

    private final String passwordHash;

    @JsonCreator
    public PasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordHash that = (PasswordHash) o;
        return Objects.equals(passwordHash, that.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passwordHash);
    }
}
