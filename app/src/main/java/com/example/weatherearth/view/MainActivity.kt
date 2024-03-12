@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.example.weatherearth.view


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.weatherearth.model.WeatherResponse
import com.example.weatherearth.viewmodel.WeatherViewModel

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
                // Ekranı oluştur
                MainScreen()
        }
    }
}





@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen() {

    val viewModel:WeatherViewModel = viewModel()
    val weatherData: WeatherResponse? by viewModel.weatherData.observeAsState()

    val searchCity= remember {
        mutableStateOf("")
    }

    val keyboardController=LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF58E3F),

                        Color(0xFF800037)// Turuncu

                    )
                )
            )

    ) {
        Spacer(modifier = Modifier.padding(vertical = 20.dp))

        TextField(value = searchCity.value, onValueChange = {searchCity.value = it}
            , modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 15.dp)
            , textStyle = TextStyle(
                textAlign = TextAlign.Left,
                fontSize = TextUnit(20f,TextUnitType.Sp)
            )
            , placeholder = { Text(text = "Enter City")}
            , label = {
                      Text(text = "Your City",)
            }
            , leadingIcon = {
                            Icon(imageVector = Icons.TwoTone.Search, contentDescription = "Search"
                                , modifier = Modifier.clickable {
                                    keyboardController?.hide()
                                    viewModel.getWeather(searchCity.value, "dc5068f715a15316a6fb4421f132a9b8")

                                }
                            )
            }, trailingIcon = {
                              Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
            },
             colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.LightGray
            )
            , shape = CircleShape
            , keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,

            )
            , keyboardActions = KeyboardActions {
                keyboardController?.hide()
                viewModel.getWeather(searchCity.value, "dc5068f715a15316a6fb4421f132a9b8")
            }
        )

        

        Spacer(modifier = Modifier.padding(vertical = 70.dp))

        if (weatherData!=null){
            weatherData?.let {
                val kelvinTemperature = it.main.temp
                val celsiusTemperature = kelvinTemperature-273.15
                val formattedTemperature = "%.1f".format(celsiusTemperature)
                TempText(temp = formattedTemperature)
                Spacer(modifier = Modifier.padding(vertical = 100.dp))
                GetStateData(weatherData = it)
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                GetWeatherData(weatherData = it)
            }
        }else{
            Text(
                "0.0 °C",
                fontSize = TextUnit(60f, TextUnitType.Sp),
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .alpha(0.5f)
            )
            Spacer(modifier = Modifier.padding(vertical = 105.dp))
            StateTitle()
            Spacer(modifier = Modifier.padding(vertical = 5.dp))
            WeatherTitles()
        }

    }

}

@Composable
fun GetWeatherData(weatherData: WeatherResponse){

    Card(
        modifier = Modifier
            .padding(15.dp)
            .heightIn(50.dp, 500.dp)
            .width(370.dp)

    ){
        Column(
            modifier = Modifier
                .background(color = Color(0x95943423))
                .fillMaxWidth()
                , verticalArrangement = Arrangement.spacedBy((-8).dp)
        ) {
            weatherData.let { item ->
                ResponseText("TimeZone       : ${item.timeZone}")
                ResponseText("Visibility         : ${item.visibility}")
                ResponseText("Latitude       : ${item.coord.lat}")
                ResponseText("Longitude       : ${item.coord.lon}")

                item.weather.forEach{ weather ->

                    ResponseText("Description    : ${weather.description}")
                }
            }
        }

    }

}

@Composable
fun GetStateData(weatherData: WeatherResponse){
    Card(
        modifier = Modifier
            .padding(15.dp)
            .heightIn(50.dp, 200.dp)
            .width(370.dp)


    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0x95943423))
                .height(80.dp),
            verticalArrangement = Arrangement.spacedBy((-12).dp)
        ) {
            Row {
                Text(
                    text = weatherData.sys.country,
                    fontSize = TextUnit(22f, TextUnitType.Sp),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(7.dp)
                        .widthIn(10.dp, 200.dp)
                        .height(30.dp)
                        .padding(horizontal = 5.dp)
                )
                Spacer(modifier = Modifier.padding(horizontal = 90.dp))
                weatherData.let {item ->
                    item.weather.forEach{weather ->
                        WeatherIcon(iconCode = weather.icon)
                    }
                }
            }
            Row{
                        Text(weatherData.name
                            , fontSize = TextUnit(20f, TextUnitType.Sp)
                            , fontFamily = FontFamily.SansSerif
                            , color = Color.DarkGray
                            , fontWeight = FontWeight.ExtraBold
                            , modifier = Modifier
                                .padding(3.dp)
                                .widthIn(10.dp, 200.dp)
                                .height(30.dp)
                                .padding(horizontal = 8.dp)
                        )

                Spacer(modifier = Modifier.padding(horizontal = 83.dp))
                weatherData.let { item ->
                    item.weather.forEach{weather ->
                        Text(
                            weather.main
                            , fontSize = TextUnit(18f, TextUnitType.Sp)
                            , fontFamily = FontFamily.SansSerif
                            , color = Color.DarkGray
                            , fontWeight = FontWeight.ExtraBold
                            , modifier = Modifier
                                .padding(7.dp)
                                .widthIn(10.dp, 200.dp)
                                .height(30.dp)
                                .padding(top = 5.dp)
                        )
                    }
                }

            }
        }
    }
}
@Composable
fun WeatherTitles() {

        Card(
            modifier = Modifier
                .padding(15.dp)
                .heightIn(60.dp, 250.dp)
                .width(370.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0x95943423))
                , verticalArrangement = Arrangement.spacedBy((-5).dp)
            ) {
                ResponseText("Latitude         :")
                ResponseText("Longitude       :")
                ResponseText("TimeZone       :")
                ResponseText("Visibility         :")
                ResponseText("Description    :")
            }

        }


}

@Composable
fun StateTitle(){
    Card(
        modifier = Modifier
            .padding(15.dp)
            .heightIn(50.dp, 200.dp)
            .width(370.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0x95943423))
                .height(80.dp)
                , verticalArrangement = Arrangement.spacedBy((-12).dp)

        ) {
            Row(

            ) {
                Text("GB:"
                    , fontSize = TextUnit(22f, TextUnitType.Sp)
                    , fontFamily = FontFamily.SansSerif
                    , color = Color.Black
                    , fontWeight = FontWeight.ExtraBold
                    , modifier = Modifier
                        .padding(10.dp)
                        .padding(horizontal = 12.dp)
                        .widthIn(10.dp, 200.dp)
                        .height(30.dp)

                )
                Spacer(modifier = Modifier.padding(horizontal = 40.dp))
            }
            Row(

            ){
                Text("City:"
                    , fontSize = TextUnit(18f, TextUnitType.Sp)
                    , fontFamily = FontFamily.SansSerif
                    , color = Color.Black
                    , fontWeight = FontWeight.ExtraBold
                    , modifier = Modifier
                        .padding(3.dp)
                        .padding(horizontal = 25.dp)
                        .widthIn(10.dp, 200.dp)
                        .height(30.dp)

                )
                Spacer(modifier = Modifier.padding(horizontal = 40.dp))
                Text("State:"
                    , fontSize = TextUnit(18f, TextUnitType.Sp)
                    , fontFamily = FontFamily.SansSerif
                    , color = Color.Black
                    , fontWeight = FontWeight.ExtraBold
                    , modifier = Modifier
                        .padding(10.dp)
                        .widthIn(10.dp, 200.dp)
                        .height(30.dp)

                )

            }


        }
    }
}
@Composable
fun ResponseText(text: String){

    Text(
        text = text
        , modifier = Modifier
            .widthIn(10.dp, 250.dp)
            .height(35.dp)
            .padding(5.dp)
            .padding(horizontal = 20.dp)
        , fontSize = TextUnit(14f, TextUnitType.Sp)
        , fontFamily = FontFamily.SansSerif
        , color = Color.DarkGray
        , fontWeight = FontWeight.ExtraBold

    )
}

@Composable
fun WeatherIcon(iconCode:String){

    val density = LocalDensity.current.density

    val iconUrl="https://openweathermap.org/img/w/$iconCode.png"
    val iconSize = (18 * density).dp
    
    val painter= rememberImagePainter(data = iconUrl, builder = {
        crossfade(true)
    })
    Card(
        modifier = Modifier
            .size(iconSize)
            .padding(1.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = "Weather Icon",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()

        )
    }

}

@Composable
fun TempText(temp:String){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(0.5f)
    ) {
        Text(
            "$temp°C",
            fontSize = TextUnit(60f, TextUnitType.Sp),
            fontFamily = FontFamily.SansSerif
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainScreen()
}