package org.wiky.letscorp.data.model;

/**
 * Created by wiky on 7/24/16.
 */
public class Query {
    public String query;
    public long timestamp;

    public Query(String query){
        this(query, System.currentTimeMillis());
    }

    public Query(String query, long timestamp){
        this.query=query;
        this.timestamp=timestamp;
    }
}
