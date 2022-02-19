package de.baderrabea.cashmachine.ui;

import de.baderrabea.cashmachine.model.CashMachine;
import de.baderrabea.cashmachine.model.CashMachineException;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CashMachineUI extends Application {
	BorderPane root = new BorderPane();
	HBox hBoxCenter;

	private CashMachine cm;
	
	private Label labelTop;
	private Label labelBottom;
	private Label labelCenter;
	
	private TextField textFieldCard;
	private TextField textFieldPin;

	private Button buttonExit;
	private Button buttonInsertCard;
	private Button buttonPin;
	private Button buttonWithdraw;
	private Button buttonInformation;
	private Button buttonWithdraw5;
	private Button buttonWithdraw10;
	private Button buttonWithdraw20;
	private Button buttonWithdraw50;
	private Button buttonWithdraw100;

	private Font fontlabel = Font.font("Verdana", FontWeight.BOLD, 60);
	private Font fontInformation = Font.font("Verdana", 30);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		cm = new CashMachine(MockFactory.createMockingAccountMap());
		primaryStage.setTitle("CashMachineUI v0.1");
		primaryStage.setScene(new Scene(createSceneGraph()));
		primaryStage.setFullScreen(true);
		primaryStage.show();
	}

	@Override
	public void init() throws Exception {
		initTopAndBottom();
	}

	private Parent createSceneGraph() {
		root = new BorderPane();
		
		root.setPadding(new Insets(60, 160, 60, 160));
		root.setTop(labelTop);
		BorderPane.setAlignment(labelTop, Pos.CENTER);
		root.setRight(buttonExit);
		BorderPane.setAlignment(buttonExit, Pos.TOP_RIGHT);
		root.setBottom(labelBottom);
		BorderPane.setAlignment(labelBottom, Pos.CENTER);

		GridPane gridPaneLeft = new GridPane();
		gridPaneLeft.setMinWidth(100);
		GridPane gridPaneRight = new GridPane();
		gridPaneRight.setMinWidth(100);

		root.setLeft(gridPaneLeft);
		
		createInsertCashCardCenter();
		return root;
	}

	private void initTopAndBottom() {
		setLabelTop("Welcome to the bank.");
		setLabelBottom("Please enter your account number.");

		buttonExit = createButton(120, 120, "X");
		
		buttonExit.setVisible(false);
		buttonExit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					cm.ejectCashCard();
					createRestartCenter();

				} catch (CashMachineException e) {
					setLabelBottomInformation(e.getMessage(), true);
					e.printStackTrace();
				}
			}
		});
	}

	private void createInsertCashCardCenter() {
		textFieldCard = createTextField(120, 300, 6);

		buttonInsertCard = createButton(120, 120, "Enter");
		
		buttonInsertCard.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					cm.insertCashCard(MockFactory.createMockingCashCard(textFieldCard.getText()));
					createOptionCenter();

				} catch (CashMachineException e) {
					setLabelBottomInformation(e.getMessage(), true);
					e.printStackTrace();
				}
			}
		});

		hBoxCenter = createCenter();
		hBoxCenter.getChildren().addAll(textFieldCard, buttonInsertCard);
		root.setCenter(hBoxCenter);
	}

	private void createOptionCenter() {
		buttonExit.setVisible(true);
		setLabelTop("Card of the account " + cm.getCashCard().getDetails().getIban() + " recognized.");
		setLabelBottom("Please select a feature.");

		buttonWithdraw = createButton(120, 120, "Withdraw");
		buttonWithdraw.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				createEnterPinCenter();
			}
		});

		buttonInformation = createButton(120, 120, "i");
		
		buttonInformation.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					setLabelBottomInformation(cm.accountStatement(), false);

				} catch (CashMachineException e) {
					setLabelBottomInformation(e.getMessage(), true);
					e.printStackTrace();
				}
			}
		});

		hBoxCenter = createCenter();
		hBoxCenter.getChildren().addAll(buttonInformation, buttonWithdraw);
		root.setCenter(hBoxCenter);
	}

	private void createEnterPinCenter() {
		setLabelBottom("Please enter your pin.");

		textFieldPin = createTextField(120, 250, 4);

		buttonPin = createButton(120, 120, "Enter");
		buttonPin.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					cm.enter(Integer.parseInt(textFieldPin.getText()));

					switch (cm.getPinSecurity()) {
					case 1:
						setLabelBottomInformation("Pin entered wrong once", false);
						break;

					case 2:
						setLabelBottomInformation("Pin entered wrong twice", false);
						break;
					}

					if (cm.isPinCorrect()) {
						createWithdrawCenter();
					}

				} catch (CashMachineException e) {
					setLabelBottomInformation(e.getMessage(), true);
					e.printStackTrace();
					buttonExit.setVisible(false);
					textFieldPin.setDisable(true);
					buttonPin.setDisable(true);
					Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2500), ae -> createRestartCenter()));
					timeline.play();
				}
			}
		});

		hBoxCenter = createCenter();
		hBoxCenter.getChildren().addAll(textFieldPin, buttonPin);
		root.setCenter(hBoxCenter);
	}

	private void createWithdrawCenter() {
		setLabelBottom("How much money do you want to withdraw?");

		buttonWithdraw5 = createButton(120, 120, "5");
		buttonWithdraw5.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				withdrawButton(5);
			}
		});

		buttonWithdraw10 = createButton(120, 120, "10");
		buttonWithdraw10.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				withdrawButton(10);
			}
		});

		buttonWithdraw20 = createButton(120, 120, "20");
		buttonWithdraw20.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				withdrawButton(20);
			}
		});

		buttonWithdraw50 = createButton(120, 120, "50");
		buttonWithdraw50.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				withdrawButton(50);
			}
		});

		buttonWithdraw100 = createButton(120, 120, "100");
		buttonWithdraw100.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				withdrawButton(100);
			}
		});

		hBoxCenter = createCenter();
		hBoxCenter.getChildren().addAll(buttonWithdraw5, buttonWithdraw10, buttonWithdraw20, buttonWithdraw50,
				buttonWithdraw100);
		root.setCenter(hBoxCenter);
	}

	private void withdrawButton(int amount) {
		try {
			cm.withdraw(amount);
			setLabelBottomInformation(cm.accountStatement(), false);
			cm.ejectCashCard();
			setLabelTop("Process completed.");
			buttonExit.setVisible(false);
			root.getCenter().setVisible(false);

			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2500), ae -> createRestartCenter()));
			timeline.play();

		} catch (CashMachineException e) {
			setLabelBottomInformation(e.getMessage(), true);
			e.printStackTrace();
			hBoxCenter.setDisable(true);
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2500), ae -> createWithdrawCenter()));
			timeline.play();
		}
	}

	private void createRestartCenter() {
		buttonExit.setVisible(false);
		setLabelTop("Welcome to the bank.");
		setLabelBottom("Please enter your account number.");
		setLabelCenter("Have a nice day.");

		hBoxCenter = createCenter();
		hBoxCenter.getChildren().addAll(labelCenter);
		root.setCenter(hBoxCenter);

		setLabelTop("");
		setLabelBottom("");

		FadeTransition ft = new FadeTransition(Duration.millis(3000), root);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.play();

		ft.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				createInsertCashCardCenter();
				initTopAndBottom();
				FadeTransition ft2 = new FadeTransition(Duration.millis(3000), root);
				ft2.setFromValue(0.0);
				ft2.setToValue(1.0);
				ft2.play();
			}
		});
	}

	private HBox createCenter() {
		hBoxCenter = new HBox();
		hBoxCenter.setAlignment(Pos.CENTER);
		hBoxCenter.setSpacing(50);
		return hBoxCenter;
	}

	private TextField createTextField(int prefHeight, int prefWidth, int validation) {
		TextField textField = new TextField();
		textField.setPrefHeight(prefHeight);
		textField.setPrefWidth(prefWidth);
		textField.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation(validation));
		textField.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
		return textField;
	}



	private Button createButton(int height, int width, String text) {
		Button button = new Button(text);
		button.setFont(fontlabel);
		button.setMinSize(height, width);
		return button;
	}

	

	private void setLabelBottom(String text) {
		if (labelBottom == null) {
			labelBottom = new Label(text);
			labelBottom.setMinHeight(200);
			labelBottom.setWrapText(true);
			labelBottom.setFont(fontlabel);
		} else {
			labelBottom.setText(text);
			labelBottom.setFont(fontlabel);
			labelBottom.setTextFill(Color.BLACK);
		}
	}

	private void setLabelTop(String text) {
		if (labelTop == null) {
			labelTop = new Label(text);
			labelTop.setMinHeight(200);
			labelTop.setFont(fontlabel);
		} else {
			labelTop.setText(text);
			labelTop.setFont(fontlabel);
			labelBottom.setTextFill(Color.BLACK);
		}
	}

	private void setLabelCenter(String text) {
		if (labelCenter == null) {
			labelCenter = new Label(text);
			labelCenter.setMinHeight(200);
			labelCenter.setFont(fontlabel);
		} else {
			labelCenter.setText(text);
			labelCenter.setFont(fontlabel);
		}
	}

	private void setLabelBottomInformation(String text, boolean failure) {
		if (labelBottom == null) {
			labelBottom = new Label(text);
			labelBottom.setMinHeight(200);
			labelBottom.setWrapText(true);
			labelBottom.setFont(fontInformation);
		} else {
			labelBottom.setText(text);
			labelBottom.setFont(fontInformation);
			labelBottom.setTextFill(Color.BLACK);
		}
		if (failure) {
			labelBottom.setTextFill(Color.RED);
		}
	}
	
	private EventHandler<KeyEvent> numeric_Validation(final Integer maxLengh) {
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				TextField txt_TextField = (TextField) e.getSource();
				if (txt_TextField.getText().length() >= maxLengh) {
					e.consume();
				}
				if (!e.getCharacter().matches("[0-9]")) {
					e.consume();
				}
			}
		};
	}
}