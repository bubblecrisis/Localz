package nabhack.localz.models;

/*
POST /deviceSignIn

Signs in deviceID when app initially starts to get accessToken. Access token will be returned in cookie and response.
PW: suggest to add location:"lat,lng"  this could be used to set areaID on each session. 

Example request:
{ 
deviceId:"520a01e1c5b56578f6000008",  
deviceKey:"1238sdf89234sdx2348sd9df3489d80324112489df82349f34kjlkjsdflkkj32l4jksdfjlkasrj23i4u908vlkxcvnw4hoxvc23730498diofhkan34lku341098sdifjenr23;i4uu09s8irw3j4alkfjaoiu234jsf97a9d8vaw4kllfa9sdv09080823ulajsflasj"
}

Example response:
{
  code:0
  data:{
     success:true
  }
}
 */
public class DeviceSignInRequest {

}
