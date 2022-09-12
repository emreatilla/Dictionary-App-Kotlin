# English Dictionary App Kotlin
In this project I made English Dictionary Application in Kotlin Language with using MVVM architecture. I fetch data from api which is [here][1]. The api contains the meaning, definition, phonetics, pronunciation(.mp3) of the word. I used all data in this project.

Libraries Used
--------------
* [Architecture][10] - A collection of libraries that help you design robust, testable, and maintainable apps.
    * [Lifecycles][11] - Create a UI that automatically responds to lifecycle events.
    * [ViewModel][12] - Easily schedule asynchronous tasks for optimal execution.
    * [LiveData][13] - Build data objects that notify views when the underlying database changes.
* Database
    * [SQLite][20] Used in this project for storing history and saved words in local database. 
* Third party and miscellaneous libraries
    * [Retrofit][30] for turns your HTTP API into a Java interface
    * [Gson][31] for convert Java Objects into their JSON representation
    * [RxJava][32] for composing asynchronous and event-based programs by using observable sequences.
    * [Lottie][33] Lottie is a mobile library for Android and iOS that parses Adobe After Effects animations exported as json renders them natively on mobile!


Architecture
--------------
The app uses [MVVM architecture][10] to have a unidirectional live of data, separation of concern, testability, and a lot more.


Pages of The Application and User Actions
--------------
* Home Page
    * From the search section at the top of the page, the user can search for the words they want. Just below the search section, the user can see the searches he/she has made before. There is a daily word part in the middle of the page. In this section, the user can copy, listen, save or share the daily words. At the bottom of the page, there is a section showing some of the words that the user has saved. User can unsave the word by clicking bookmark icon in this part. Additionally all words are clickable in history, daily word and favorites tabs.
* Word Page
    * This page only shows up when the user is looking for the meaning of a word. On this page, the meaning of the word is displayed to the user. The user can copy, listen, save or share the word.
* Favorites Page
    * On this page, all the words that the user has saved are displayed to the user. User can unsave words by clicking bookmark icon from this page too.
* History Page
    * On this page, all the words that the user has searched before are displayed to the user. User can delete words from history by simply swiping words to left.The user can delete the entire history with the trash can icon in the upper right corner of the page. The application prompts the user before deleting all history.

App GIF
--------------
![]()




[1]: https://dictionaryapi.dev/
[10]: https://developer.android.com/topic/architecture
[11]: https://developer.android.com/jetpack/androidx/releases/lifecycle
[12]: https://developer.android.com/topic/libraries/architecture/viewmodel
[13]: https://developer.android.com/topic/libraries/architecture/livedata
[20]: https://www.sqlite.org/index.html
[30]: https://square.github.io/retrofit/
[31]: https://github.com/google/gson
[32]: https://github.com/ReactiveX/RxJava
[33]: https://github.com/airbnb/lottie-android
