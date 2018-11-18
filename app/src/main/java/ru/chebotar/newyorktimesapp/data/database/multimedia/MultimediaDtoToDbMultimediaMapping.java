package ru.chebotar.newyorktimesapp.data.database.multimedia;


import androidx.annotation.NonNull;
import ru.chebotar.newyorktimesapp.data.database.Mapping;
import ru.chebotar.newyorktimesapp.data.network.models.MultimediaDTO;

public class MultimediaDtoToDbMultimediaMapping extends Mapping<MultimediaDTO, DbMultimedia> {

    @NonNull
    @Override
    public DbMultimedia map(@NonNull final MultimediaDTO multimediaDTO) {
        return new DbMultimedia(
                multimediaDTO.getUrl(),
                multimediaDTO.getType(),
                multimediaDTO.getHeight(),
                multimediaDTO.getWidth());
    }
}
