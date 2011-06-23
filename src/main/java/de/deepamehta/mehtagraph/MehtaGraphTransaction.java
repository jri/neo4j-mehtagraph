package de.deepamehta.mehtagraph;



public interface MehtaGraphTransaction {

    void success();

    void failure();

    void finish();
}
