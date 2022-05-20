package org.ergoplatform.mosaik.backenddemo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class MosaikService {
    private final HashMap<String, List<String>> guidVisitors = new HashMap<>();

    public List<String> getVisitors(String guid) {
        // returns visitors for a given session guid. For the sake of simplicity, this is never
        // purged and just hold in memory. For a real-world example, you would use a datbase and
        // a scheduled task to clean entries not accessed for a long time.

        synchronized (guidVisitors) {
            List<String> retList = guidVisitors.get(guid);

            if (retList == null) {
                retList = new ArrayList<>();
                guidVisitors.put(guid, retList);
            }

            return retList;
        }
    }
}
