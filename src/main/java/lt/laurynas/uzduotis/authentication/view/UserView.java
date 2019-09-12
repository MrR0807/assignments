package lt.laurynas.uzduotis.authentication.view;

import lt.laurynas.uzduotis.authentication.entity.User;

public class UserView {

    private final Long id;
    private final String email;
    private final String password;

    public UserView(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static UserView mapFromEntity(User user) {
        return new UserView(user.getId(), user.getEmail(), user.getPassword());
    }

    @Override
    public String toString() {
        return "UserView{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
