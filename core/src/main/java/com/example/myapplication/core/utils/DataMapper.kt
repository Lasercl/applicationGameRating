import com.example.myapplication.core.data.domain.model.Game
import com.example.myapplication.core.data.source.local.entity.GameEntity
import com.example.myapplication.core.data.source.remote.response.GamesDetailResponse
import com.example.myapplication.core.data.source.remote.response.ResultsItem

object DataMapper {

    // 🔹 API DTO ke Entity (biasanya dari /games)
    fun mapResponsesToEntities(input: List<ResultsItem>): List<GameEntity> {
        val gamesList = ArrayList<GameEntity>()
        input.map {
            val game = GameEntity(
                gamesId = it.id,
                name = it.name,
                image = it.backgroundImage,
                released = it.released,
                rating = it.rating,
                ratingsCount = it.ratingsCount,
                description = null, // belum ada di list API
                isFavorite = false
            )
            gamesList.add(game)
        }
        return gamesList
    }

    // 🔹 API DTO ke Entity (untuk detail /games/{id})
    fun mapDetailToEntity(input: GamesDetailResponse): GameEntity =
        GameEntity(
            gamesId = input.id,
            name = input.name,
            image = input.backgroundImage,
            released = input.released,
            rating = input.rating,
            ratingsCount = null,
            description = input.description,
            isFavorite = false
        )

    // 🔹 Entity ke Domain
    fun mapEntitiesToDomain(input: List<GameEntity>): List<Game> =
        input.map {
            Game(
                id = it.gamesId,
                name = it.name,
                backgroundImage = it.image,
                released = it.released,
                rating = it.rating,
                ratingsCount = it.ratingsCount,
                description = it.description,
                isFavorite = it.isFavorite
            )
        }

    // 🔹 Domain ke Entity (misal waktu update favorite)
    fun mapDomainToEntity(input: Game): GameEntity =
        GameEntity(
            gamesId = input.id,
            name = input.name,
            image = input.backgroundImage,
            released = input.released,
            rating = input.rating,
            ratingsCount = input.ratingsCount,
            description = input.description,
            isFavorite = input.isFavorite
        )
    fun mapEntityToDomain(input: GameEntity): Game =
        Game(
            id = input.gamesId,
            name = input.name,
            backgroundImage = input.image,
            released = input.released,
            rating = input.rating,
            ratingsCount = input.ratingsCount,
            description = input.description,
            isFavorite = input.isFavorite
        )
}
