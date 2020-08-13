package org.json.simple.parser;

import java.io.IOException;

public interface ContentHandler {
  void startJSON() throws ParseException, IOException;
  
  void endJSON() throws ParseException, IOException;
  
  boolean startObject() throws ParseException, IOException;
  
  boolean endObject() throws ParseException, IOException;
  
  boolean startObjectEntry(String paramString) throws ParseException, IOException;
  
  boolean endObjectEntry() throws ParseException, IOException;
  
  boolean startArray() throws ParseException, IOException;
  
  boolean endArray() throws ParseException, IOException;
  
  boolean primitive(Object paramObject) throws ParseException, IOException;
}


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\org\json\simple\parser\ContentHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */