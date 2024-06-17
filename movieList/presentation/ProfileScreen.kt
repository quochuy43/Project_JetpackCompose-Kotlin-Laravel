package com.example.project_appmovie.movieList.presentation

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.project_appmovie.ui.theme.dark_blue
import com.example.project_appmovie.R
import com.example.project_appmovie.database.account.AccountViewModel
import com.example.project_appmovie.database.HomeViewModel
import com.example.project_appmovie.dialog.CustomDialogFavourite
import com.example.project_appmovie.dialog.CustomDialogUIAboutUs
import com.example.project_appmovie.dialog.CustomDialogUISecurity
import com.example.project_appmovie.dialog.CustomDialogUISendEmail
import com.example.project_appmovie.ui.theme.Purple40
import com.example.project_appmovie.ui.theme.blue
import com.example.project_appmovie.ui.theme.bluee
import com.example.project_appmovie.ui.theme.dark_red
import com.example.project_appmovie.ui.theme.orange
import com.example.project_appmovie.ui.theme.redd
import com.example.project_appmovie.ui.theme.white_yellow
import com.example.project_appmovie.ui.theme.yellow_message
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ProfileScreen (navController: NavHostController, homeViewModel: HomeViewModel = hiltViewModel()){

    // Log Out
    val accountViewModel: AccountViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    var showDialogLogout by remember { mutableStateOf(false) }

    // Thanh cuộn
    var scrollState = rememberScrollState()

    // Khai bao các biến để lấy thông tin người dùng từ HomeViewModel
    val userEmail by homeViewModel.userEmail.observeAsState("Unknown Email")
    val userPass by homeViewModel.userPass.observeAsState("Unknown Pass")
    val userName by homeViewModel.userName.observeAsState("Unknown Name")
    val userPhone by homeViewModel.userPhone.observeAsState(0)
    val userAge by homeViewModel.userAge.observeAsState(0)

    // Chọn ảnh từ thư viện ảnh
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val defaulAvatarBitmap = painterResource(id = R.drawable.def_avatar)
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    // Nút để hiển thị alert để sửa thông tin
    var infoVisible by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(userName) }
    var email by remember { mutableStateOf(userEmail) }
    var phone by remember { mutableStateOf(userPhone.toString()) }
    var age by remember { mutableStateOf(userAge.toString()) }
    var oldpassword by remember { mutableStateOf("") }
    var newpassword by remember { mutableStateOf("") }
    var password by remember { mutableStateOf(userPass) }

    LaunchedEffect(userEmail) {
        accountViewModel.loadUserAvatar(userEmail)
    }

    // Bien de mo alert about us
    var openAlertAboutUs = remember { mutableStateOf(false) }
    var openAlertSecurity = remember { mutableStateOf(false) }
    var openAlertSendEmail = remember { mutableStateOf(false) }
    var openAlertFavouriteList = remember { mutableStateOf(false) }
    
    Column (
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "PROFILE", fontSize = 24.sp, color = Color.Gray, modifier = Modifier.padding(top = 15.dp))
        Spacer(modifier = Modifier.height(10.dp))

        imageUri?.let { uri ->
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

            bitmap.value?.let { btm ->
                val avatarBase64 = bitmapToBase64(btm)
                coroutineScope.launch {
                    accountViewModel.updateAccountAvatar(userEmail, avatarBase64)
                }
            }
        }
        // Hien thi avatar
        Box(
            modifier = Modifier
                .size(120.dp)
                .padding(5.dp)
                .border(1.dp, white_yellow, CircleShape)
                .clip(CircleShape), // Đặt hình tròn cho hình ảnh
            contentAlignment = Alignment.Center
        ) {
            val avatarBitmap = accountViewModel.userAvatar.collectAsState().value
            if (avatarBitmap != null) {
                Image(
                    bitmap = avatarBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = defaulAvatarBitmap,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        IconButton(onClick = {launcher.launch("image/*")}, modifier = Modifier.offset(y = (-10).dp)) {
            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "pick_image", tint = white_yellow)
        }

        Text(text = userName, fontWeight = FontWeight.Bold, fontSize = 27.sp, color = white_yellow, modifier = Modifier
            .padding(top = 10.dp)
            .offset(y = (-20).dp))
        Text(text = userEmail, fontWeight = FontWeight.Bold, fontSize = 19.sp, color = white_yellow, modifier = Modifier.offset(y = (-20).dp))

        Spacer(modifier = Modifier.height(20.dp))

        // Xử lí chỉnh sửa thông tin cá nhân
        ElevatedButton(onClick = { infoVisible = true }, colors = ButtonDefaults.buttonColors(bluee), modifier = Modifier
            .offset(y = (-20).dp), shape = CircleShape) {
            Text(text = "EDIT PROFILE", textAlign = TextAlign.Center, fontSize = 16.sp, color = dark_blue, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(10.dp))
            Icon(imageVector = Icons.Default.Edit, contentDescription = "edit profile", tint = dark_blue)
        }

        if (infoVisible) {
            AlertDialog(
                onDismissRequest = { infoVisible = false },
                title = { Text("Update Profile") },
                text = {
                    Column {
                        TextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Enter a new name") }
                        )
                        TextField(
                            value = age,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() }) {
                                    age = newValue
                                }
                            },
                            label = { Text("Enter a new age") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        TextField(
                            value = phone,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() }) {
                                    phone = newValue
                                }
                            },
                            label = { Text("Enter a new phone number") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        TextField(
                            value = oldpassword,
                            onValueChange = { oldpassword = it },
                            label = { Text("Enter the old password") }
                        )
                        TextField(
                            value = newpassword,
                            onValueChange = { newpassword = it },
                            label = { Text("Enter the new password") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        // Update user information using HomeViewModel
                        if (oldpassword.equals(password)) {
                            coroutineScope.launch {
                                accountViewModel.updateUserInfo(
                                    name,
                                    age.toIntOrNull() ?: 0,
                                    phone.toIntOrNull() ?: 0,
                                    newpassword,
                                    email
                                )
                            }
                            Toast.makeText(context, "You were updated information successfully, please log in again!", Toast.LENGTH_SHORT).show()
                            infoVisible = false
                            navController.navigate("login")
                        }
                        else {
                            Toast.makeText(context, "Your old password is incorrect, please try again", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(orange)) {
                        Text("SAVE")
                    }
                }
            )
        }

        Box (modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Purple40)) {
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically){
                    Box(modifier = Modifier
                        .size(50.dp)
                        .padding(5.dp)
                        .background(dark_blue, shape = CircleShape)
                        .clip(CircleShape), contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Rounded.Notifications, contentDescription = "Notifications")
                    }
                    // Text:
                    Text(text = "Notifications", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier
                        .padding(start = 14.dp)
                        .weight(1f))
                    val checkednotice = remember {
                        mutableStateOf(false)
                    }
                    Switch(checked = checkednotice.value, onCheckedChange = { checkednotice.value = it }, colors = SwitchDefaults.colors(dark_blue))
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically){
                    Box(modifier = Modifier
                        .size(50.dp)
                        .padding(5.dp)
                        .background(orange, shape = CircleShape)
                        .clip(CircleShape), contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Filled.PrivacyTip, contentDescription = "PrivacyTip")
                    }
                    // Text:
                    Text(text = "Private Account", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier
                        .padding(start = 14.dp)
                        .weight(1f))
                    val checkAccount = remember {
                        mutableStateOf(false)
                    }
                    Switch(checked = checkAccount.value, onCheckedChange = { checkAccount.value = it }, colors = SwitchDefaults.colors(orange))
                }
            }
        }

        // Tiep theo nua...
        Spacer(modifier = Modifier.height(5.dp))
        Box (modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Purple40)) {
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically){
                    Box(modifier = Modifier
                        .size(50.dp)
                        .padding(5.dp)
                        .background(redd, shape = CircleShape)
                        .clip(CircleShape), contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Rounded.Movie, contentDescription = "List favourite Movies")
                    }
                    // Text:
                    Text(text = "My Playlist", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier
                        .padding(start = 14.dp)
                        .weight(1f))
                    IconButton(onClick = { openAlertFavouriteList.value = true }) {
                        Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = "Favourite", tint = Color.White)
                    }
                    if (openAlertFavouriteList.value) {
                        CustomDialogFavourite(openDialogBox = openAlertFavouriteList, navController = navController)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically){
                    Box(modifier = Modifier
                        .size(50.dp)
                        .padding(5.dp)
                        .background(dark_red, shape = CircleShape)
                        .clip(CircleShape), contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Rounded.Lock, contentDescription = "Security")
                    }
                    // Text:
                    Text(text = "Security & Privacy", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier
                        .padding(start = 14.dp)
                        .weight(1f))
                    IconButton(onClick = { openAlertSecurity.value = true }) {
                        Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = "Privacy", tint = Color.White)
                    }
                    if (openAlertSecurity.value) {
                        CustomDialogUISecurity(openDialogBox = openAlertSecurity)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically){
                    Box(modifier = Modifier
                        .size(50.dp)
                        .padding(5.dp)
                        .background(yellow_message, shape = CircleShape)
                        .clip(CircleShape), contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Rounded.Message, contentDescription = "Message")
                    }
                    // Text:
                    Text(text = "Send Us a Message", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier
                        .padding(start = 14.dp)
                        .weight(1f))
                    IconButton(onClick = { openAlertSendEmail.value = true }) {
                        Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = "Message", tint = Color.White)
                    }
                    if (openAlertSendEmail.value) {
                        CustomDialogUISendEmail(openDialogBox = openAlertSendEmail)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically){
                    Box(modifier = Modifier
                        .size(50.dp)
                        .padding(5.dp)
                        .background(blue, shape = CircleShape)
                        .clip(CircleShape), contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Filled.People, contentDescription = "About Us")
                    }
                    // Text:
                    Text(text = "About Us", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier
                        .padding(start = 14.dp)
                        .weight(1f))
                    IconButton(onClick = { openAlertAboutUs.value = true }) {
                        Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = "About Us", tint = Color.White)
                    }
                    if (openAlertAboutUs.value) {
                        CustomDialogUIAboutUs(openDialogBox = openAlertAboutUs)
                    }
                }
            }
        }

        // Cuoi cung
        Spacer(modifier = Modifier.height(5.dp))
        Box (modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Purple40)) {
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically){
                    Box(modifier = Modifier
                        .size(50.dp)
                        .padding(5.dp)
                        .background(dark_red, shape = CircleShape)
                        .clip(CircleShape), contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Rounded.Logout, contentDescription = "Logout")
                    }
                    // Text:
                    Text(text = "Log Out", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier
                        .padding(start = 14.dp)
                        .weight(1f))
                    // Xu li logout
                    IconButton(onClick = {
                        showDialogLogout = true
                    }) {
                        Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = "Logout", tint = Color.White)
                    }
                }
            }
        }
        if (showDialogLogout) {
            AlertDialog(
                onDismissRequest = { showDialogLogout = false },
                confirmButton = {
                    TextButton(onClick = {
                        coroutineScope.launch {
                            accountViewModel.logOut()
                            navController.navigate("login") {
                                popUpTo("main") { inclusive = true }
                            }
                        }
                    }){
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialogLogout = false }) {
                        Text("No")
                    }
                },
                title = { Text("Logout Confirmation")},
                text = { Text("Are you sure you want to logout?") }
            )
        }
    }
}

// Chuyểnddoi 1 hình ảnh thành chuối để lưu vào db
fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
}

// Chuyển doi chuoi thanh hinh anh de hien thi ra man hinh
fun base64ToBitMap(base64String: String): Bitmap? {
    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

