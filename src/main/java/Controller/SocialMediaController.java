package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    public MessageService messageService;
    public AccountService accountService;

    public SocialMediaController() {
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postNewUser);
        app.post("/login", this::postUserLogin);
        app.post("/messages", this::postNewMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_ID}", this::getMessagebyID);
        app.delete("/messages/{message_ID}", this::deleteMessagebyID);
        app.patch("/messages/{message_ID}", this::updateMessagebyID);
        app.get("/accounts/{account_id}/messages", this::getMessagesbyAccountID);

        return app;
    }

    /**
     * #1. Handler to process a new User Registration
     * 
     * The registration will be successful if and only if the username is not 
     * blank, the password is at least 4 characters long, and an Account with 
     * that username does not already exist. If all these conditions are met, 
     * the response body should contain a JSON of the Account, including its 
     * account_id. The response status should be 200 OK, which is the default. 
     * The new account should be persisted to the database.
     * 
     * If the registration is not successful, the response status should be 400. (Client error)
     * 
     * @param ctx context object
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    private void postNewUser(Context ctx) throws JsonMappingException, JsonProcessingException {
        // create an objectmapper to get the context body
        ObjectMapper objMapper = new ObjectMapper();
        Account account = objMapper.readValue(ctx.body(), Account.class);
        //check if username is blank or the password is less than 4 size
        if(account.getUsername().isBlank() || account.getPassword().length() < 4) {
            ctx.status(400);
            return;
        }
        else {
            //return the newly inserted user or return 400
            Account newAccount = accountService.insertUser(account);
            if(newAccount == null) {
                ctx.status(400);
            }
            else {
                ctx.json(objMapper.writeValueAsString(newAccount));
            }
        }
    }

    /**
     * #2. Process User Logins
     * 
     * The login will be successful if and only if the username and password 
     * provided in the request body JSON match a real account existing on the 
     * database. If successful, the response body should contain a JSON of the 
     * account in the response body, including its account_id. The response status 
     * should be 200 OK, which is the default.
     * 
     * If the login is not successful, the response status should be 401. (Unauthorized)
     * 
     * @param ctx context object
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    private void postUserLogin(Context ctx) throws JsonMappingException, JsonProcessingException {
        // create an objectmapper to get the context body
        ObjectMapper objMapper = new ObjectMapper();
        Account account = objMapper.readValue(ctx.body(), Account.class);
        //return the user if verified or null if not
        Account newAccount = accountService.verifyUser(account);
        if(newAccount == null) {
            ctx.status(401);
        }
        else {
            ctx.json(objMapper.writeValueAsString(newAccount));
        }
    }

    /**
     * #3. Process Creation of New Messages
     * 
     * The creation of the message will be successful if and only if the message_text 
     * is not blank, is under 255 characters, and posted_by refers to a real, existing 
     * user. If successful, the response body should contain a JSON of the message, 
     * including its message_id. The response status should be 200, which is the default. 
     * The new message should be persisted to the database.
     * 
     * If the creation of the message is not successful, the response status should be 400. (Client error)
     * 
     * @param ctx context object
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    private void postNewMessage(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objMapper = new ObjectMapper();
        Message message = objMapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.insertMessage(message);
        if(addedMessage == null) {
            ctx.status(400);
        }
        else {
            ctx.json(objMapper.writeValueAsString(addedMessage));
        }
    }

    /**
     * #4. Retrieve All Messages
     * 
     * The response body should contain a JSON representation of a list containing all 
     * messages retrieved from the database. It is expected for the list to simply be 
     * empty if there are no messages. The response status should always be 200, which 
     * is the default.
     * 
     * @param ctx context object
     */
    private void getAllMessages(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    /**
     * #5. Retrieve Message by ID
     * 
     * The response body should contain a JSON representation of the message 
     * identified by the message_id. It is expected for the response body to 
     * simply be empty if there is no such message. The response status should 
     * always be 200, which is the default.
     * 
     * @param ctx context object
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    private void getMessagebyID(Context ctx) throws JsonMappingException, JsonProcessingException {
        int id = Integer.parseInt(ctx.pathParam("message_ID"));
        Message getMessage = messageService.getMessageByID(id);
        if(getMessage == null) {
            ctx.status(400);
        }
        else {
            ctx.json(getMessage);
        }
    }

    /**
     * #6. Delete Message by ID
     * 
     * The deletion of an existing message should remove an existing 
     * message from the database. If the message existed, the response 
     * body should contain the now-deleted message. The response status 
     * should be 200, which is the default.
     * 
     * If the message did not exist, the response status should be 200, but 
     * the response body should be empty. This is because the DELETE verb is 
     * intended to be idempotent, ie, multiple calls to the DELETE endpoint 
     * should respond with the same type of response.
     * 
     * @param ctx context object
     */
    private void deleteMessagebyID(Context ctx) {

    }

    /**
     * #7. Update Message by ID
     * 
     * The update of a message should be successful if and only if the message id already 
     * exists and the new message_text is not blank and is not over 255 characters. If the 
     * update is successful, the response body should contain the full updated message 
     * (including message_id, posted_by, message_text, and time_posted_epoch), and the 
     * response status should be 200, which is the default. The message existing on the 
     * database should have the updated message_text.
     * 
     * If the update of the message is not successful for any reason, the response status should be 400. (Client error)
     * 
     * @param ctx context object
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    private void updateMessagebyID(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objMapper = new ObjectMapper();
        Message message = objMapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_ID"));
        //check if message.message_text is empty or greater than 255
        if(message.message_text.isBlank() || message.getMessage_text().length() >= 255) {
            ctx.status(400);
        }
        else {
            Message updateMessage = messageService.updateMessageByID(message_id, message);
            if(updateMessage == null) {
                ctx.status(400);
            }
            else {
                ctx.json(objMapper.writeValueAsString(updateMessage));
            }
        }
    }

    /**
     * #8. Retrieve All Messages by Particular User
     * 
     * The response body should contain a JSON representation of a list containing 
     * all messages posted by a particular user, which is retrieved from the database. 
     * It is expected for the list to simply be empty if there are no messages. The 
     * response status should always be 200, which is the default.
     * 
     * @param ctx context object
     */
    private void getMessagesbyAccountID(Context ctx) {

    }
}