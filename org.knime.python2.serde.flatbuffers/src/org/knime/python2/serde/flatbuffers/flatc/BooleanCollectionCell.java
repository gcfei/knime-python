// automatically generated by the FlatBuffers compiler, do not modify

package org.knime.python2.serde.flatbuffers.flatc;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class BooleanCollectionCell extends Table {
  public static BooleanCollectionCell getRootAsBooleanCollectionCell(ByteBuffer _bb) { return getRootAsBooleanCollectionCell(_bb, new BooleanCollectionCell()); }
  public static BooleanCollectionCell getRootAsBooleanCollectionCell(ByteBuffer _bb, BooleanCollectionCell obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public BooleanCollectionCell __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public boolean value(int j) { int o = __offset(4); return o != 0 ? 0!=bb.get(__vector(o) + j * 1) : false; }
  public int valueLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer valueAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public boolean missing(int j) { int o = __offset(6); return o != 0 ? 0!=bb.get(__vector(o) + j * 1) : false; }
  public int missingLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer missingAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public boolean keepDummy() { int o = __offset(8); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }

  public static int createBooleanCollectionCell(FlatBufferBuilder builder,
      int valueOffset,
      int missingOffset,
      boolean keepDummy) {
    builder.startObject(3);
    BooleanCollectionCell.addMissing(builder, missingOffset);
    BooleanCollectionCell.addValue(builder, valueOffset);
    BooleanCollectionCell.addKeepDummy(builder, keepDummy);
    return BooleanCollectionCell.endBooleanCollectionCell(builder);
  }

  public static void startBooleanCollectionCell(FlatBufferBuilder builder) { builder.startObject(3); }
  public static void addValue(FlatBufferBuilder builder, int valueOffset) { builder.addOffset(0, valueOffset, 0); }
  public static int createValueVector(FlatBufferBuilder builder, boolean[] data) { builder.startVector(1, data.length, 1); for (int i = data.length - 1; i >= 0; i--) builder.addBoolean(data[i]); return builder.endVector(); }
  public static void startValueVector(FlatBufferBuilder builder, int numElems) { builder.startVector(1, numElems, 1); }
  public static void addMissing(FlatBufferBuilder builder, int missingOffset) { builder.addOffset(1, missingOffset, 0); }
  public static int createMissingVector(FlatBufferBuilder builder, boolean[] data) { builder.startVector(1, data.length, 1); for (int i = data.length - 1; i >= 0; i--) builder.addBoolean(data[i]); return builder.endVector(); }
  public static void startMissingVector(FlatBufferBuilder builder, int numElems) { builder.startVector(1, numElems, 1); }
  public static void addKeepDummy(FlatBufferBuilder builder, boolean keepDummy) { builder.addBoolean(2, keepDummy, false); }
  public static int endBooleanCollectionCell(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}
