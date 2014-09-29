net.cloudpath.xpressconnect
===========================

This repo contains the result of an analysis for scholarly and security comment of the workings of the publicly available Android app with id net.cloudpath.xpressconnect. 

https://play.google.com/store/apps/details?id=net.cloudpath.xpressconnect

The analysis was performed on the apk file with the following properties:

```shell
$ md5 net.cloudpath.xpressconnect-1.apk 
MD5 (net.cloudpath.xpressconnect-1.apk) = adddde5a408b13d5ce4866eac662b626

$ aapt dump badging net.cloudpath.xpressconnect-1.apk 
package: name='net.cloudpath.xpressconnect' versionCode='500000115' versionName='5.0.115'
sdkVersion:'7'
targetSdkVersion:'19'
uses-permission:'android.permission.READ_LOGS'
uses-permission:'android.permission.ACCESS_NETWORK_STATE'
uses-permission:'android.permission.INTERNET'
uses-permission:'android.permission.ACCESS_BACKGROUND_SERVICE'
uses-permission:'android.permission.ACCESS_WIFI_STATE'
uses-permission:'android.permission.CHANGE_WIFI_STATE'
uses-permission:'android.permission.WAKE_LOCK'
uses-permission:'android.permission.CHANGE_NETWORK_STATE'
uses-permission:'android.permission.NFC'
application-label:'XpressConnect'
application-icon-160:'res/drawable/xpc_applicationicon.png'
application: label='XpressConnect' icon='res/drawable/xpc_applicationicon.png'
launchable-activity: name='net.cloudpath.xpressconnect.screens.EntryScreen'  label='XpressConnect' icon=''
uses-feature:'android.hardware.wifi'
uses-implied-feature:'android.hardware.wifi','requested android.permission.ACCESS_WIFI_STATE, android.permission.CHANGE_WIFI_STATE, or android.permission.CHANGE_WIFI_MULTICAST_STATE permission'
uses-feature:'android.hardware.touchscreen'
uses-implied-feature:'android.hardware.touchscreen','assumed you require a touch screen unless explicitly made optional'
main
device-admin
other-activities
other-receivers
other-services
supports-screens: 'small' 'normal' 'large' 'xlarge'
supports-any-density: 'true'
locales: '--_--'
densities: '160'
```


license
===========================

The Fair Use Act states "an act of circumvention that is carried out to gain access to a work of substantial public interest solely for purposes of criticism, comment, news reporting, scholarship, or research"

This analysis is made available for the purpose of criticism, comment, scholarship, and research.


