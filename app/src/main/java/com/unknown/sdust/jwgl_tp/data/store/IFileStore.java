package com.unknown.sdust.jwgl_tp.data.store;

public interface IFileStore<K> {
    void fromFile(K file);
    K toFile();
}
