package com.omoskalchuk.weatherapp.WeatherApp.view;

import com.omoskalchuk.weatherapp.WeatherApp.controller.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SpringUI(path = "")
public class MainView extends UI {
    @Autowired
    private WeatherService weatherService;
    private VerticalLayout mainLayout;
    private NativeSelect<String> unitSelect;
    private  TextField cityTextField;
    private Button showWeatherButton;
    private Label currentLocationTitle;
    private Label currentTemp;
    private Label weatherDescription;
    private Label weatherMin;
    private Label weatherMax;
    private Label pressureLabel;
    private Label humidityLabel;
    private Label windSpeedLabel;
    private Label sunRiseLabel;
    private Label sunSetLabel;
    private ExternalResource img;
    private Image iconImage;
    private HorizontalLayout formLayout;
    private HorizontalLayout logoLayout;
    private HorizontalLayout headerLayout;
    private HorizontalLayout dashBoardMain;
    private HorizontalLayout mainDescriptionLayout;
    private VerticalLayout descriptionLayout;
    private VerticalLayout pressureLayout;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setUpLayout();
        setHeader();
        setLogo();
        setUpForm();
        dashBoardTitle();
        dashBoardDescription();

        showWeatherButton.addClickListener(event -> {
            if (!cityTextField.getValue().equals("")) {
                try {
                    updateUI();
//                    dashBoardDescription();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else Notification.show("Please enter a city");
        });

    }



    private void setUpLayout(){


        iconImage = new Image();

        weatherDescription = new Label("Description: Clear Skies");
        weatherMin = new Label("Min: 13 C");
        weatherMax = new Label("Max: 20 C");
        pressureLabel = new Label("Pressure: 123pa");
        humidityLabel = new Label("Humidity 59%");
        windSpeedLabel = new Label("Wind is calm");
        sunRiseLabel = new Label("Sunrise: ");
        sunSetLabel = new Label("Sunset: ");


        mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        setContent(mainLayout);
    }
    private void setHeader() {

        headerLayout = new HorizontalLayout();
        headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Label title = new Label("Weather!");
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        title.addStyleName(ValoTheme.LABEL_COLORED);


        headerLayout.addComponent(title);

        mainLayout.addComponent(headerLayout);

    }
    private void setLogo() {
        logoLayout = new HorizontalLayout();
        logoLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Image icon = new Image(null,new ClassResource("/weather.png"));
        icon.setWidth("125px");
        icon.setHeight("125px");

        logoLayout.addComponent(icon);

        mainLayout.addComponent(logoLayout);
    }
    private void setUpForm() {
        formLayout = new HorizontalLayout();
        formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        //Create the selection component
        unitSelect = new NativeSelect<>();
        unitSelect.setWidth("45px");
        ArrayList<String> items = new ArrayList<>();
        items.add("C");
        items.add("F");

        unitSelect.setItems(items);
        unitSelect.setValue(items.get(0));

        formLayout.addComponent(unitSelect);

        //Add textField
        cityTextField = new TextField();
        cityTextField.setWidth("80%");
        cityTextField.setValue("Stamford");
        formLayout.addComponent(cityTextField);

        //Add button
        showWeatherButton = new Button();
        showWeatherButton.setIcon(VaadinIcons.SEARCH);

        formLayout.addComponent(showWeatherButton);

        mainLayout.addComponent(formLayout);
    }
    private void dashBoardTitle() {

        dashBoardMain = new HorizontalLayout();
        dashBoardMain.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        //Adding Icon


        currentLocationTitle = new Label("Currently in Spokane");
        currentLocationTitle.addStyleName(ValoTheme.LABEL_H2);
        currentLocationTitle.addStyleName(ValoTheme.LABEL_LIGHT);

        //Current Temp Label
        currentTemp = new Label("10 C");
        currentTemp.addStyleName(ValoTheme.LABEL_BOLD);
        currentTemp.addStyleName(ValoTheme.LABEL_H1);
        currentTemp.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

    }
    private void dashBoardDescription() {

        mainDescriptionLayout = new HorizontalLayout();
        mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        //Description Vertical Layout
        descriptionLayout = new VerticalLayout();
        descriptionLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        descriptionLayout.addComponent(weatherDescription);
        descriptionLayout.addComponent(weatherMin);
        descriptionLayout.addComponent(weatherMax);

        //Pressure, humidityLabel etc..
        pressureLayout = new VerticalLayout();
        pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        pressureLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        pressureLayout.addComponent(pressureLabel);
        pressureLayout.addComponent(humidityLabel);
        pressureLayout.addComponent(windSpeedLabel);
        pressureLayout.addComponent(sunRiseLabel);
        pressureLayout.addComponent(sunSetLabel);

    }

    private void updateUI() throws JSONException {

        String city = cityTextField.getValue().trim();
        String defaultUnit;
        String unit;

        if (unitSelect.getValue().equals("C")){
            defaultUnit = "metric";
            unit = "\u00b0" + "C";
        } else{
            defaultUnit = "imperial";
            unit = "\u00b0" + "F";
        }

        weatherService.setCityName(city);
        weatherService.setUnit(defaultUnit);

        currentLocationTitle.setValue("Currently in " + city);

        JSONObject myObject = weatherService.returnMainObject();
        int temp = myObject.getInt("temp");

        currentTemp.setValue(temp + unit);

        //Get min, max, pressure, humidity
        JSONObject mainObject = weatherService.returnMainObject();

        double minTemp = mainObject.getDouble("temp_min");
        double maxTemp = mainObject.getDouble("temp_max");
        int humidity = mainObject.getInt("humidity");
        int pressure = mainObject.getInt("pressure");

        // Get Wind Speed

        JSONObject windObject = weatherService.returnWindObject();
        double wind = windObject.getDouble("speed");

        //Get sunrise sunset

        JSONObject systemObj = weatherService.returnSunSetObject();
        long sunRise = systemObj.getLong("sunrise") * 1000;
        long sunSet = systemObj.getLong("sunset") * 1000;


//        try {
//        System.out.println("Data " + weatherService.getWeather("Ternopil").getString("coord").toString());
//        JSONArray jsonArray = weatherService.returnWeatherArray("Ternopil");
//        JSONObject windObj = weatherService.returnWindObject("Ternopil");
//
////        System.out.println("Pressure: " + myObject.getLong("pressure") +
////                " Temperature: "+ myObject.getLong("temp") +
////                " humidityLabel: " + myObject.getInt("humidityLabel"));
//
//        System.out.println("Wind speed: " + windObj.getDouble("speed") +
//                " Deg: " + windObj.getDouble("deg"));
////
//
////
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
////
////
////    }
//
//
//            System.out.println("Data " + weatherService.getWeather("Ternopil").getString("coord").toString());
//
//    JSONArray jsonArray = weatherService.returnWeatherArray("Stamford");
//    JSONObject myObject = weatherService.returnMainObject("Stamford");
//    JSONObject windObj = weatherService.returnWindObject("Stamford");
//
//            System.out.println("Pressure: " + myObject.getLong("pressure") +
//                    " Temperature: "+ myObject.getLong("temp") +
//                    " humidityLabel: " + myObject.getInt("humidityLabel"));
//
//            System.out.println("Wind speed: " + windObj.getDouble("speed") +
//                    " Deg: " + windObj.getDouble("deg"));
//
//            for (int i = 0; i < jsonArray.length(); i++){
//
//        JSONObject weatherObject = jsonArray.getJSONObject(i);
//
//        System.out.println("Weather = " + weatherObject.getInt("id") +
//                ", main " + weatherObject.getString("main") +
//                ", description: " + weatherObject.getString("description"));
//
//    }

        //Setup icon image
        String iconCode = "";
        String description = "";
        JSONArray jsonArray = weatherService.returnWeatherArray();

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject weatherObject = jsonArray.getJSONObject(i);

            description = weatherObject.getString("description");
            iconCode = weatherObject.getString("icon");
        }
        iconImage.setSource(new ExternalResource("http://openweathermap.org/img/w/" + iconCode + ".png"));

        dashBoardMain.addComponents(currentLocationTitle,iconImage,currentTemp);
        mainLayout.addComponent(dashBoardMain);

    // Update description UI
                weatherDescription.setValue("Condition: " + description);
                weatherDescription.addStyleName(ValoTheme.LABEL_SUCCESS);
                weatherMin.setValue("Min: " + String.valueOf(minTemp) + unit);
                weatherMax.setValue("Max: " + String.valueOf(maxTemp) + unit);
                pressureLabel.setValue("Pressure: " + String.valueOf(pressure + " hpa"));
                humidityLabel.setValue("Humidity: " + String.valueOf(humidity) + " %");
                windSpeedLabel.setValue("Wind: " + String.valueOf(wind) + " meter/s");

                sunRiseLabel.setValue("Sunrise: " + convertTime(sunRise));
                sunSetLabel.setValue("Sunset: " + convertTime(sunSet));

        mainDescriptionLayout.addComponents(descriptionLayout,pressureLayout);
        mainLayout.addComponent(mainDescriptionLayout);
    }

    private String convertTime(long time){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy hh.mm aa");

        return dateFormat.format(new Date(time));

    }

}














