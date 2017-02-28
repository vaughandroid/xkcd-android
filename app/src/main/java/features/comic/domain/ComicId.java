package features.comic.domain;

import com.google.auto.value.AutoValue;

import java.io.Serializable;

@AutoValue
public abstract class ComicId implements Serializable {

    public static ComicId create(int intVal) {
        return new AutoValue_ComicId(intVal);
    }

    public abstract int intVal();
}
