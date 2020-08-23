package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HomePage {
    @FindBy(id="logout-button")
    private WebElement logoutBtn;

    @FindBy(id="nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id="add-new-note")
    private WebElement addNewNoteBtn;

    @FindBy(id="note-title")
    private WebElement noteTitleInput;

    @FindBy(id="note-description")
    private WebElement noteDescTextArea;

    @FindBy(id="note-save-changes")
    private WebElement noteSaveChangesBtn;

    @FindBy(className="displayed-note")
    private List<WebElement> displayedNotes;

    @FindBy(id="nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id="add-new-credential")
    private WebElement addNewCredentialBtn;

    @FindBy(id="credential-url")
    private WebElement credentialUrlInput;

    @FindBy(id="credential-username")
    private WebElement credentialUsernameInput;

    @FindBy(id="credential-password")
    private WebElement credentialPasswordInput;

    @FindBy(id="credential-save-changes")
    private WebElement credentialSaveChangesBtn;

    @FindBy(className="displayed-credential")
    private List<WebElement> displayedCredentials;

    private WebDriverWait wait;

    private EncryptionService testEncryptionService;

    public HomePage(WebDriver driver){
        PageFactory.initElements(driver,this);
        wait = new WebDriverWait(driver, 10);
        testEncryptionService = new EncryptionService();
    }

    public void logoutUser(){
        logoutBtn.click();
    }

    public void createNote(String noteTitle, String noteDescription){
        // click on notes tab
        notesTab.click();

        // click add new note
        wait.until(ExpectedConditions.elementToBeClickable(addNewNoteBtn));
        addNewNoteBtn.click();

        //populate form with note and submit
        wait.until(ExpectedConditions.elementToBeClickable(noteTitleInput));
        noteTitleInput.sendKeys(noteTitle);
        noteDescTextArea.sendKeys(noteDescription);
        noteSaveChangesBtn.click();
    }

    public boolean isNoteDisplayed(String noteTitle, String noteDescription){
        // click on notes tab
        notesTab.click();

        //check if note is displayed
        wait.until(ExpectedConditions.visibilityOfAllElements(displayedNotes));
        for( WebElement displayedNote: displayedNotes){
            WebElement displayedTitle = displayedNote.findElement(By.className("note-title"));
            WebElement displayedDesc = displayedNote.findElement(By.className("note-description"));

            if(displayedTitle.getText().equals(noteTitle) &&
            displayedDesc.getText().equals(noteDescription)){
                return true;
            }
        }
        return false;
    }

    public void editNote(String oldNoteTitle, String newNoteTitle, String oldNoteDesc, String newNoteDesc){
        // click on notes tab
        notesTab.click();

        //find note
        wait.until(ExpectedConditions.visibilityOfAllElements(displayedNotes));
        for( WebElement displayedNote: displayedNotes){
            WebElement displayedTitle = displayedNote.findElement(By.className("note-title"));
            WebElement displayedDesc = displayedNote.findElement(By.className("note-description"));

            if(displayedTitle.getText().equals(oldNoteTitle) &&
                    displayedDesc.getText().equals(oldNoteDesc)){
                WebElement editNoteBtn = displayedNote.findElement(By.className("note-edit"));
                editNoteBtn.click();

                //edit form
                wait.until(ExpectedConditions.elementToBeClickable(noteTitleInput));
                if(newNoteTitle != null ){
                    noteTitleInput.clear();
                    noteTitleInput.sendKeys(newNoteTitle);
                }
                if(newNoteDesc != null){
                    noteDescTextArea.clear();
                    noteDescTextArea.sendKeys(newNoteDesc);
                }
                noteSaveChangesBtn.click();
                return;
            }
        }
    }

    public void deleteNote(String noteTitle, String noteDescription){
        // click on notes tab
        notesTab.click();

        //find note
        wait.until(ExpectedConditions.visibilityOfAllElements(displayedNotes));
        for( WebElement displayedNote: displayedNotes){
            WebElement displayedTitle = displayedNote.findElement(By.className("note-title"));
            WebElement displayedDesc = displayedNote.findElement(By.className("note-description"));

            if(displayedTitle.getText().equals(noteTitle) &&
                    displayedDesc.getText().equals(noteDescription)){
                WebElement deleteNoteElem = displayedNote.findElement(By.className("note-delete"));
                deleteNoteElem.click();
                return;
            }
        }

    }

    public void createCredential(String url, String username,String password){
        // click on credentials tab
        credentialsTab.click();

        // click add new credential
        wait.until(ExpectedConditions.elementToBeClickable(addNewCredentialBtn));
        addNewCredentialBtn.click();

        //populate form with credential and submit
        wait.until(ExpectedConditions.elementToBeClickable(credentialUrlInput));
        credentialUrlInput.sendKeys(url);
        credentialUsernameInput.sendKeys(username);
        credentialPasswordInput.sendKeys(password);
        credentialSaveChangesBtn.click();
    }

    public boolean isCredentialDisplayedAndPasswordEncrypted(String expectedUrl, String expectedUsername, String expectedPassword){

        WebElement displayedCredential = getDisplayedCredential(expectedUrl,expectedUsername,expectedPassword);
        return displayedCredential != null;
    }

    public boolean isCredentialViewableAndPasswordUnencrypted(String url, String username,
                                                              String password){

        WebElement displayedCredential = getDisplayedCredential(url,username,password);

        if(displayedCredential != null){
            WebElement editCredentialBtn = displayedCredential.findElement(By.className("credential-edit"));
            editCredentialBtn.click();

            //view credential
            wait.until(ExpectedConditions.elementToBeClickable(credentialUrlInput));
            if(credentialUrlInput.getAttribute("value").equals(url) &&
                    credentialUsernameInput.getAttribute("value").equals(username) &&
                    credentialPasswordInput.getAttribute("value").equals(password)){ //checks if viewable password is unencrypted
                return true;
            }
        }
        return false;
    }

    public void editCredentials(String oldUrl, String oldUsername, String oldPassword,
                                String newUrl, String newUsername, String newPassword){

        WebElement displayedCredential = getDisplayedCredential(oldUrl,oldUsername,
                oldPassword);

        if(displayedCredential != null){
            WebElement editCredentialBtn = displayedCredential.findElement(By.className("credential-edit"));
            editCredentialBtn.click();

            //edit credential
            wait.until(ExpectedConditions.elementToBeClickable(credentialUrlInput));
            credentialUrlInput.clear();
            credentialUrlInput.sendKeys(newUrl);

            credentialUsernameInput.clear();
            credentialUsernameInput.sendKeys(newUsername);

            credentialPasswordInput.clear();
            credentialPasswordInput.sendKeys(newPassword);

            credentialSaveChangesBtn.click();
        }
    }

    public void deleteCredential(String url, String username, String password){

        WebElement displayedCredential = getDisplayedCredential(url,username,
                password);

        if(displayedCredential != null){
            WebElement deleteCredentialElem = displayedCredential.findElement(By.className("credential-delete"));
            deleteCredentialElem.click();
        }
    }

    private WebElement getDisplayedCredential(String expectedUrl, String expectedUsername,
                                              String expectedPassword){
        // click on credentials tab
        credentialsTab.click();

        //check if credential is displayed
        wait.until(ExpectedConditions.visibilityOfAllElements(displayedCredentials));
        for( WebElement displayedCredential: displayedCredentials){
            WebElement displayedUrl = displayedCredential.findElement(By.className("credential-url"));
            WebElement displayedUsername = displayedCredential.findElement(By.className("credential-username"));
            WebElement displayedPassword = displayedCredential.findElement(By.className("credential-password"));
            WebElement credentialKeyElem = displayedCredential.findElement(By.className("credential-key"));

            String expectedEncryptedPassword = testEncryptionService.encryptValue(expectedPassword,
                    credentialKeyElem.getAttribute("id"));

            if(displayedUrl.getText().equals(expectedUrl) &&
                    displayedUsername.getText().equals(expectedUsername) &&
                    displayedPassword.getText().equals(expectedEncryptedPassword)){ //checks if displayed password is encrypted
                return displayedCredential;
            }
        }

        return null;
    }
}

