import { StatusBar } from 'expo-status-bar';
import React, { useState} from 'react';
import { StyleSheet, Text, View, TextInput, Dimensions, TouchableOpacity, ScrollView} from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
const Stack = createNativeStackNavigator();


function UsernameTab({navigation}) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  return (
    <View style={styles.container}>
      <Text style={styles.text}>Please login to access your calendar</Text>
      <TextInput 
        style={styles.loginBox}
        placeholder="Username"
        onChangeText={text => setUsername(text)}
        defaultValue={username}
        autoCorrect={false}
        autoCapitalize='none'
      />
      <TextInput 
        style={styles.loginBox}
        placeholder="Password"
        onChangeText={text => setPassword(text)}
        defaultValue={password}
        autoCorrect={false}
        autoCapitalize='none'
        // secureTextEntry={true}
      />
      <TouchableOpacity 
      style={styles.button}
      onPress={() => checkInfoCorrect(username, password) ? navigation.navigate("CalendarPage") : alert("wrong password")}>
        <Text style={[styles.text, {marginTop: 0, marginBottom: 0}]}>Submit</Text>
      </TouchableOpacity>

    </View>
  );
}

function CalendarTab() {
  return (
  <ScrollView>
    <Text style={styles.text}>Calendar here</Text>
  </ScrollView>
  );
}

function checkInfoCorrect(username, password) {
  return (username == "hi" && password == "bye");
}
// export default usernameTab;
export default function App() {
  return(
  <NavigationContainer>
    <Stack.Navigator initialRouteName="Login">
      <Stack.Screen style={styles.container} name="Login" component={UsernameTab} />
      <Stack.Screen name="CalendarPage" component={CalendarTab} />
    </Stack.Navigator>
  </NavigationContainer>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'darkkhaki',
    alignItems: 'center',
    // justifyContent: 'center',
  },
  loginBox: {
    // marginTop: 100,
    height: 40,
    width: Dimensions.get("window").width* 7 / 8,
    justifyContent: "center",
    textAlign:"center",
    borderRadius: 20,
    color: "darkred",
    backgroundColor: "cornsilk",
    marginBottom: 10
  },
  text: {
    marginTop: 200,
    fontSize: 20,
    marginBottom: 10,
    textAlign: "center",
    width: Dimensions.get("window").width / 2 ,
  },
  button: {
    marginTop:10,
    // alignContent: "right",
    backgroundColor: "darkseagreen",
    padding: 10, // increases the size of the button 
    borderRadius: 20, // makes it a rounded button instead of a rectangle
  },

});

