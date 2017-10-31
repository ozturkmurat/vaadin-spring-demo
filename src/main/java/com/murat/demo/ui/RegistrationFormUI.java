package com.murat.demo.ui;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.murat.demo.model.User;
import com.murat.demo.persistence.UserRepository;
import com.murat.demo.security.authentication.AccessControl;
import com.murat.demo.security.authentication.BasicAccessControl;
import com.murat.demo.ui.LoginScreen.LoginListener;
import com.murat.demo.util.EmailOrPhoneValidator;
import com.murat.demo.util.PasswordUtil;
import com.murat.demo.util.PasswordValidator;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Binder;
import com.vaadin.data.Binder.Binding;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.BindingValidationStatus.Status;
import com.vaadin.data.HasValue;
import com.vaadin.data.Validator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Title("Registration Form")
@Theme("registration")
public class RegistrationFormUI extends CssLayout {

    private static final int WIDTH = 350;

    private final Binder<User> binder = new Binder<>();
    @Autowired
    UserRepository userRepository;
    
    private Binding<User, String> passwordBinding;
    private Binding<User, String> confirmPasswordBinding;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField phoneField;
    private TextField emailField;
    PasswordField passwordField;
    private boolean showConfirmPasswordStatus;
    
    
    private static final String VALID = "valid";

    private void addToLayout(Layout layout, AbstractTextField textField,
            String placeHolderText) {
        textField.setPlaceholder(placeHolderText);
        Label statusMessage = new Label();
        statusMessage.setVisible(false);
        statusMessage.addStyleName("validation-message");
        textField.setData(statusMessage);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(false);
        horizontalLayout.addComponent(textField);
        textField.setWidth(WIDTH, Unit.PIXELS);
        horizontalLayout.addComponent(statusMessage);
        layout.addComponent(horizontalLayout);
    }

    @Autowired
    public RegistrationFormUI(UserRepository repo) {
    	this.userRepository = repo;
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        addComponent(layout);

        firstNameField = new TextField();
        addToLayout(layout, firstNameField, "First name");

        binder.forField(firstNameField).asRequired("First name may not be empty")
                .withValidationStatusHandler(
                        status -> commonStatusChangeHandler(status,
                        		firstNameField))
                .bind(User::getFirstName, User::setFirstName);
        
        lastNameField = new TextField();
        addToLayout(layout, lastNameField, "Last name");

        binder.forField(lastNameField).asRequired("Last name may not be empty")
                .withValidationStatusHandler(
                        status -> commonStatusChangeHandler(status,
                        		lastNameField))
                .bind(User::getLastName, User::setLastName);

        phoneField = new TextField();
        addToLayout(layout, phoneField, "Phone");
        binder.forField(phoneField)
                
                .bind(User::getPhone, User::setPhone);
        
        emailField = new TextField();
        addToLayout(layout, emailField, "E-Mail");
        binder.forField(emailField)
                .withValidator(new EmailOrPhoneValidator())
                .withValidationStatusHandler(
                        status -> commonStatusChangeHandler(status,
                        		emailField))
                .bind(User::getEmail, User::setEmail);

        passwordField = new PasswordField();
        addToLayout(layout, passwordField, "Password");
        passwordBinding = binder.forField(passwordField)
                .withValidator(new PasswordValidator())
                .withValidationStatusHandler(
                        status -> commonStatusChangeHandler(status,
                                passwordField))
                .bind(User::getPassword, User::setPassword);
        passwordField.addValueChangeListener(
                event -> confirmPasswordBinding.validate());

        PasswordField confirmPasswordField = new PasswordField();
        addToLayout(layout, confirmPasswordField, "Password again");

        confirmPasswordBinding = binder.forField(confirmPasswordField)
                .withValidator(Validator.from(this::validateConfirmPasswd,
                        "Password doesn't match"))
                .withValidationStatusHandler(
                        status -> confirmPasswordStatusChangeHandler(status,
                                confirmPasswordField))
                .bind(User::getPassword, (person, pwd) -> {
                });
        
        layout.addComponent(createButton());

        firstNameField.focus();

        binder.setBean(new User());
    }    
    
	private Button createButton() {
        Button button = new Button("Sign Up");
        button.addStyleName(ValoTheme.BUTTON_PRIMARY);
        button.setWidth(WIDTH, Unit.PIXELS);
        button.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				boolean userExists = userRepository.findUserByEmail(emailField.getValue()) != null;
				
					
					if(!userExists){
						User user = createUser(firstNameField.getValue(),lastNameField.getValue(),phoneField.getValue(),emailField.getValue(),passwordField.getValue());
						userRepository.save(user);	
						Notification.show("Sign Up Successful");
						AccessControl accessControl = new BasicAccessControl(userRepository);
						
						LoginListener listener = new LoginListener() {

							@Override
							public void loginSuccessful() {

								((MyUI) UI.getCurrent()).showMainScreen();
							}
						};
						((MyUI) UI.getCurrent()).setContent(
								new LoginScreen(accessControl,listener ));
					}
					else{
						Notification.show("This e-mail is already is in use");
					}
								
			}
		});
        return button;
    }
	
	public User createUser(String firstName,String lastName,String phone,String email,String password){
		User user = new User();
		user.setFirstName(firstNameField.getValue());
		user.setLastName(lastNameField.getValue());
		user.setPhone(phoneField.getValue());
		user.setEmail(emailField.getValue());				
		user.setPassword(PasswordUtil.cryptWithMD5(passwordField.getValue()));
		user.setUuid(UUID.randomUUID().toString());
		user.setActivated(false);
		return user;
	}

    private void commonStatusChangeHandler(BindingValidationStatus<?> event,
            AbstractTextField field) {
        Label statusLabel = (Label) field.getData();
        statusLabel.setVisible(!event.getStatus().equals(Status.UNRESOLVED));
        switch (event.getStatus()) {
        case OK:
            statusLabel.setValue("");
            statusLabel.setIcon(VaadinIcons.CHECK);
            statusLabel.getParent().addStyleName(VALID);
            break;
        case ERROR:
            statusLabel.setIcon(VaadinIcons.CLOSE);
            statusLabel.setValue(event.getMessage().orElse("Unknown error"));
            statusLabel.getParent().removeStyleName(VALID);
        default:
            break;
        }
    }

    private void confirmPasswordStatusChangeHandler(
            BindingValidationStatus<?> event, AbstractTextField field) {
        commonStatusChangeHandler(event, field);
        Label statusLabel = (Label) field.getData();
        statusLabel.setVisible(showConfirmPasswordStatus);
    }

    private boolean validateConfirmPasswd(String confirmPasswordValue) {
        showConfirmPasswordStatus = false;
        if (confirmPasswordValue.isEmpty()) {
            return true;

        }
        BindingValidationStatus<String> status = passwordBinding.validate();
        if (status.isError()) {
            return true;
        }
        showConfirmPasswordStatus = true;
        HasValue<?> pwdField = passwordBinding.getField();
        return Objects.equals(pwdField.getValue(), confirmPasswordValue);
    }

    
}
