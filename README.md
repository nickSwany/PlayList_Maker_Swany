# Приложение “PlayList Maker” 
## Описание
Промежуточный проект "PlayList Maker" позволяет, при помощи API iTunes, искать и прослушщивать треки, просматривать информацию о треке, добавлять их в избранное и при этом позваляет создать, редактировать и делиться собственными плейлистами. В приложение есть настрока смена темы. 
## Скриншоты
<p align="center">
   <img src="https://github.com/nickSwany/PlayList_Maker_Swany/blob/master/app/src/main/res/screenschots/search.jpg?raw=true" alt="screenshot1" width="200"/>
   <img src="https://github.com/nickSwany/PlayList_Maker_Swany/blob/master/app/src/main/res/screenschots/settings.jpg?raw=true" alt="screenshot1" width="200"/>
   <img src="https://github.com/nickSwany/PlayList_Maker_Swany/blob/master/app/src/main/res/screenschots/playlist.jpg?raw=true" alt="screenshot1" width="200"/>
   <img src="https://github.com/nickSwany/PlayList_Maker_Swany/blob/master/app/src/main/res/screenschots/favoriteTracks.jpg?raw=true" alt="screenshot1" width="200"/>
</p>

## Технологии и подходы, использованные в проекте

В процессе разработки данного проекта я получил опыт в использовании следующих технологий и подходов:

- **Single Activity Architecture**: Изначально работал с несколькими Activity, затем перешёл на архитектурный подход Single Activity для улучшения навигации и управления состоянием приложения.
- **Retrofit**: Использовал Retrofit для взаимодействия с iTunes API.
- **Clean Architecture**: Применял принципы чистой архитектуры для разделения приложения на слои.
- **MediaPlayer**: Использовал MediaPlayer для воспроизведения треков.
- **MVP и MVVM**: Изначально использовал паттерн MVP, затем переписал приложение на MVVM для улучшения поддерживаемости и тестируемости кода.
- **Coroutines и Flow**: Реализовал асинхронные операции с использованием Coroutines и Flow для эффективного управления потоками данных.
- **Room**: Использовал базу данных Room для хранения избранных треков и созданных плейлистов.
- **Koin**: Внедрение зависимостей с помощью Koin для упрощения управления зависимостями.

## Библиотеки
Navigation, Fragment KTX, Material Components, Glide, Gson, Retrofit, Room, Kotlin Coroutines, Koin, Lifecycle ViewModel KTX, AppCompat, ConstraintLayout

## Требования
- **IDE**: Android Studio Koala
- **JDK**: Java 17
- **Kotlin**: 1.7.10
