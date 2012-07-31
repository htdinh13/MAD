package Model;

import javax.microedition.rms.*;

public class DataRecord {

    private RecordStore rs;

    public DataRecord() {
    }

    public void save(int id, String info) {

        try {
            rs = RecordStore.openRecordStore("data.sav", true);
            int nr = rs.getNumRecords();
            if (nr < id) {
                byte[] a = {};
                for (int i = 0; i < (id - nr); i++) {
                    int d = rs.addRecord(a, 0, 0);
                }
                byte[] bdt = info.getBytes();
                rs.setRecord(id, bdt, 0, bdt.length);
                rs.closeRecordStore();
                bdt = null;
            }
        } catch (Exception e) {
        }
    }

    public String load(int id) {
        String t = "";
        try {
            rs = RecordStore.openRecordStore("data.sav", true);
            byte[] bdt = rs.getRecord(id);
            t = new String(bdt, 0, bdt.length);
        } catch (Exception e) {
        } finally {
            try {
                rs.closeRecordStore();
            } catch (RecordStoreNotOpenException ex) {
                ex.printStackTrace();
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            }
        }
        return t;
    }

    public void clear() {
        try {
            rs = RecordStore.openRecordStore("data.sav", true);
            int size = rs.getSize();
            rs.closeRecordStore();
            if (size > 0) {
                RecordStore.deleteRecordStore("data.sav");
            }
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        if (rs != null) {
            try {
                rs.closeRecordStore();
            } catch (RecordStoreNotOpenException ex) {
                ex.printStackTrace();
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            }
        }
    }

    public int size() {
        int size = -1;
        try {
            rs = RecordStore.openRecordStore("data.sav", true);
            size = rs.getNumRecords();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        } finally {
            try {
                rs.closeRecordStore();
            } catch (RecordStoreNotOpenException ex) {
                ex.printStackTrace();
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            }
        }
        return size;
    }
}
