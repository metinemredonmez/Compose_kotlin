package com.metinemre.retrofitcompose

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metinemre.retrofitcompose.model.CryptoModel
import com.metinemre.retrofitcompose.services.CryptoAPI
import com.metinemre.retrofitcompose.ui.theme.RetrofitComposeTheme
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RetrofitComposeTheme {
               MainScreen()
            }
        }
    }
}


@Composable
fun MainScreen() {

    var  cryptoModels =  remember { mutableStateListOf<CryptoModel>() }

     val BASE_URL = "https://raw.githubusercontent.com/"

    // retrofit
    val  retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CryptoAPI::class.java)

    val call  =  retrofit.getData()

    call.enqueue(object: Callback<List<CryptoModel>> {
        override fun onResponse(
            call: Call<List<CryptoModel>>,
            response: Response<List<CryptoModel>>
        ) {
            if (response.isSuccessful) {
                response.body()?.let {
                        //list
                    cryptoModels.addAll(it)
                }
            }
        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            t.printStackTrace()
        }
    })
    
    // top bar! Scaffold
    Scaffold(topBar = { AppBar() }) {
        Surface {
            // list add scaffold
            CryptoList(cryptos = cryptoModels)


        }
    }
}



// for list composeble  lazy column kullanacagız!
@Composable
fun CryptoList(cryptos: List<CryptoModel>) {
  // item ıcıne ııcnctıpro lıstenın ıcne koydum tek bır crypto ıtem ın ıcınden aldık !  cok cok onemlı!
    LazyColumn(contentPadding = PaddingValues(5.dp)) {
        items(cryptos) { crypto ->
            cryptoRow(crypto = crypto)
        }
    }

}
@Composable
fun cryptoRow(crypto: CryptoModel) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colors.surface)) {
        Text(text = crypto.currency,
            color =MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold
            )
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = crypto.price,
        style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(2.dp),
            )
    }
}


@Composable
fun AppBar(){
    TopAppBar(contentPadding = PaddingValues(10.dp), backgroundColor = Color.White) {
        Text(text = "retrofit compose demo", fontSize = 26.sp)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RetrofitComposeTheme {
       cryptoRow(crypto = CryptoModel("btc","12123"))
    }
}