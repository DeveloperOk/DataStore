package com.enterprise.datastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enterprise.datastore.ui.theme.DataStoreTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DataStoreTheme {
                AppDataStore()
            }
        }
    }
}

@Composable
fun AppDataStore() {
    Scaffold(modifier = Modifier.fillMaxSize().systemBarsPadding()) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){

            MainBody()

        }
    }
}

@Composable
fun MainBody() {

    val mykey = "my_data_key"

    var textValue = rememberSaveable{ mutableStateOf("") }

    var inputValue = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = true, block = {

        GlobalScope.launch(Dispatchers.IO) {

            val readValue = AppDataStorePreferences.readStringFromDataStore(context, mykey)

            GlobalScope.launch(Dispatchers.Main) {

                //If null, empty will be set
                textValue.value = readValue ?: ""

            }
        }

    } )

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){

       Text(textValue.value)

        OutlinedTextField(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            value = inputValue.value,
            onValueChange = {
                inputValue.value = it
            },
            label = {
                Text(text = "Value to Save")
            }

        )

       Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
           onClick = {

               GlobalScope.launch(Dispatchers.IO) {

                   AppDataStorePreferences.writeStringToDataStore(context, mykey, inputValue.value.text)

                   val tempReadValue = AppDataStorePreferences.readStringFromDataStore(context, mykey)

                   GlobalScope.launch(Dispatchers.Main) {

                       textValue.value = tempReadValue ?: ""

                   }

               }

            }) {

           Text("Save")

       }

    }

}

