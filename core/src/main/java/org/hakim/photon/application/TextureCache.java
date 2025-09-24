package org.hakim.photon.application;

import org.hakim.photon.renderer.Texture;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Class representing a texture cache.
 * This can be used to have a single storage for all textures so they don't have to be reloaded from
 * disk every time they are used. This cache also keeps track of the texture units that are in use.
 * If you try to add a texture attached to a texture unit that is already in use, that will be
 * forbidden. The textures can be accessed via a name. If you try to insert a texture into this cache
 * with a name that is already contained, the old value is replaced.
 * This class is designed to be used as a singleton in your app. If there are multiple texture caches,
 * this can't guarantee that the provided texture units are indeed free.
 */
public class TextureCache {

    /** map storing the textures with access by name */
    private final HashMap<String, Texture> cache;

    /** set containing all texture units in use */
    private final HashSet<Integer> units;

    /**
     * Constructor for a texture cache.
     */
    public TextureCache() {
        this.cache = new HashMap<>();
        this.units = new HashSet<>();
    }

    /**
     * Stores a texture in this cache.
     * If the key is not yet in this cache then a new entry is created.
     * If the key is already contained, the old value is replaced and the texture unit of the old value
     * is removed from the storage.
     * It is not allowed to enter a new key with a used texture unit. Then this method throws an error.
     *
     * @param key reference for the texture
     * @param texture texture to store
     * @throws IllegalArgumentException when trying to enter a new key with a used texture unit
     */
    public void put(String key, Texture texture) throws IllegalArgumentException {
        assert key != null;
        assert texture != null;

        // if the key is new the texture unit may not exist in storage
        if (!this.cache.containsKey(key) && this.units.contains(texture.getTextureUnit())) {
            throw new IllegalArgumentException("This texture unit is already in use!");
        }

        Texture previous = this.cache.put(key, texture);

        // remove the previously occupied texture unit
        if (previous != null) {
            this.units.remove(previous.getTextureUnit());
        }
        this.units.add(texture.getTextureUnit());
    }

    /**
     * Retrieves a texture from the storage by its reference.
     * If the texture is not present in this cache, null is returned.
     *
     * @param key reference of the texture to get
     * @return the texture or null
     */
    public Texture get(String key) {
        assert key != null;

        return cache.get(key);
    }

    /**
     * Clears this cache.
     * Afterward this cache contains no entries anymore.
     * This does not free the texture resources occupied by OpenGL!
     */
    public void clear() {
        cache.clear();
        units.clear();
    }
}
