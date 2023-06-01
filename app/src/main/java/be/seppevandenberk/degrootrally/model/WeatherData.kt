package be.seppevandenberk.degrootrally.model

data class WeatherData(
    val latitude: Double,
    val longitude: Double,
    val generationtime_ms: Double,
    val utc_offset_seconds: Int,
    val timezone: String,
    val timezone_abbreviation: String,
    val elevation: Double,
    val current_weather: CurrentWeather,
    val hourly_units: HourlyUnits,
    val hourly: HourlyData,
    val daily_units: DailyUnits,
    val daily: DailyData
)

data class CurrentWeather(
    val temperature: Double,
    val windspeed: Double,
    val winddirection: Double,
    val weathercode: Int,
    val is_day: Int,
    val time: String
)

data class HourlyUnits(
    val time: String,
    val windspeed_80m: String,
    val winddirection_80m: String
)

data class HourlyData(
    val time: List<String>,
    val windspeed_80m: List<Double>,
    val winddirection_80m: List<Int>
)

data class DailyUnits(
    val time: String,
    val weathercode: String
)

data class DailyData(
    val time: List<String>,
    val weathercode: List<Int>
)