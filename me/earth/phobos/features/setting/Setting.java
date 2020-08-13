/*     */ package me.earth.phobos.features.setting;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import me.earth.phobos.event.events.ClientEvent;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Setting<T>
/*     */ {
/*     */   private final String name;
/*     */   private final T defaultValue;
/*     */   private T value;
/*     */   private T plannedValue;
/*     */   private T min;
/*     */   private T max;
/*     */   private boolean hasRestriction;
/*     */   private Predicate<T> visibility;
/*     */   private String description;
/*     */   private Feature feature;
/*     */   
/*     */   public Setting(String name, T defaultValue) {
/*  28 */     this.name = name;
/*  29 */     this.defaultValue = defaultValue;
/*  30 */     this.value = defaultValue;
/*  31 */     this.plannedValue = defaultValue;
/*  32 */     this.description = "";
/*     */   }
/*     */   
/*     */   public Setting(String name, T defaultValue, String description) {
/*  36 */     this.name = name;
/*  37 */     this.defaultValue = defaultValue;
/*  38 */     this.value = defaultValue;
/*  39 */     this.plannedValue = defaultValue;
/*  40 */     this.description = description;
/*     */   }
/*     */   
/*     */   public Setting(String name, T defaultValue, T min, T max, String description) {
/*  44 */     this.name = name;
/*  45 */     this.defaultValue = defaultValue;
/*  46 */     this.value = defaultValue;
/*  47 */     this.min = min;
/*  48 */     this.max = max;
/*  49 */     this.plannedValue = defaultValue;
/*  50 */     this.description = description;
/*  51 */     this.hasRestriction = true;
/*     */   }
/*     */   
/*     */   public Setting(String name, T defaultValue, T min, T max) {
/*  55 */     this.name = name;
/*  56 */     this.defaultValue = defaultValue;
/*  57 */     this.value = defaultValue;
/*  58 */     this.min = min;
/*  59 */     this.max = max;
/*  60 */     this.plannedValue = defaultValue;
/*  61 */     this.description = "";
/*  62 */     this.hasRestriction = true;
/*     */   }
/*     */   
/*     */   public Setting(String name, T defaultValue, T min, T max, Predicate<T> visibility, String description) {
/*  66 */     this.name = name;
/*  67 */     this.defaultValue = defaultValue;
/*  68 */     this.value = defaultValue;
/*  69 */     this.min = min;
/*  70 */     this.max = max;
/*  71 */     this.plannedValue = defaultValue;
/*  72 */     this.visibility = visibility;
/*  73 */     this.description = description;
/*  74 */     this.hasRestriction = true;
/*     */   }
/*     */   
/*     */   public Setting(String name, T defaultValue, T min, T max, Predicate<T> visibility) {
/*  78 */     this.name = name;
/*  79 */     this.defaultValue = defaultValue;
/*  80 */     this.value = defaultValue;
/*  81 */     this.min = min;
/*  82 */     this.max = max;
/*  83 */     this.plannedValue = defaultValue;
/*  84 */     this.visibility = visibility;
/*  85 */     this.description = "";
/*  86 */     this.hasRestriction = true;
/*     */   }
/*     */   
/*     */   public Setting(String name, T defaultValue, Predicate<T> visibility) {
/*  90 */     this.name = name;
/*  91 */     this.defaultValue = defaultValue;
/*  92 */     this.value = defaultValue;
/*  93 */     this.visibility = visibility;
/*  94 */     this.plannedValue = defaultValue;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  98 */     return this.name;
/*     */   }
/*     */   
/*     */   public T getValue() {
/* 102 */     return this.value;
/*     */   }
/*     */   
/*     */   public T getPlannedValue() {
/* 106 */     return this.plannedValue;
/*     */   }
/*     */   
/*     */   public void setPlannedValue(T value) {
/* 110 */     this.plannedValue = value;
/*     */   }
/*     */   
/*     */   public T getMin() {
/* 114 */     return this.min;
/*     */   }
/*     */   
/*     */   public T getMax() {
/* 118 */     return this.max;
/*     */   }
/*     */   
/*     */   public void setValue(T value) {
/* 122 */     setPlannedValue(value);
/* 123 */     if (this.hasRestriction) {
/* 124 */       if (((Number)this.min).floatValue() > ((Number)value).floatValue()) {
/* 125 */         setPlannedValue(this.min);
/*     */       }
/*     */       
/* 128 */       if (((Number)this.max).floatValue() < ((Number)value).floatValue()) {
/* 129 */         setPlannedValue(this.max);
/*     */       }
/*     */     } 
/* 132 */     ClientEvent event = new ClientEvent(this);
/* 133 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 134 */     if (!event.isCanceled()) {
/* 135 */       this.value = this.plannedValue;
/*     */     } else {
/* 137 */       this.plannedValue = this.value;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setValueNoEvent(T value) {
/* 142 */     setPlannedValue(value);
/* 143 */     if (this.hasRestriction) {
/* 144 */       if (((Number)this.min).floatValue() > ((Number)value).floatValue()) {
/* 145 */         setPlannedValue(this.min);
/*     */       }
/*     */       
/* 148 */       if (((Number)this.max).floatValue() < ((Number)value).floatValue()) {
/* 149 */         setPlannedValue(this.max);
/*     */       }
/*     */     } 
/* 152 */     this.value = this.plannedValue;
/*     */   }
/*     */   
/*     */   public void setMin(T min) {
/* 156 */     this.min = min;
/*     */   }
/*     */   
/*     */   public void setMax(T max) {
/* 160 */     this.max = max;
/*     */   }
/*     */   
/*     */   public void setFeature(Feature feature) {
/* 164 */     this.feature = feature;
/*     */   }
/*     */   
/*     */   public Feature getFeature() {
/* 168 */     return this.feature;
/*     */   }
/*     */   
/*     */   public int getEnum(String input) {
/* 172 */     for (int i = 0; i < (this.value.getClass().getEnumConstants()).length; i++) {
/* 173 */       Enum e = (Enum)this.value.getClass().getEnumConstants()[i];
/* 174 */       if (e.name().equalsIgnoreCase(input)) {
/* 175 */         return i;
/*     */       }
/*     */     } 
/* 178 */     return -1;
/*     */   }
/*     */   
/*     */   public void setEnumValue(String value) {
/* 182 */     for (Enum e : (Enum[])((Enum)this.value).getClass().getEnumConstants()) {
/* 183 */       if (e.name().equalsIgnoreCase(value)) {
/* 184 */         this.value = (T)e;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public String currentEnumName() {
/* 190 */     return EnumConverter.getProperName((Enum)this.value);
/*     */   }
/*     */   
/*     */   public int currentEnum() {
/* 194 */     return EnumConverter.currentEnum((Enum)this.value);
/*     */   }
/*     */   
/*     */   public void increaseEnum() {
/* 198 */     this.plannedValue = (T)EnumConverter.increaseEnum((Enum)this.value);
/* 199 */     ClientEvent event = new ClientEvent(this);
/* 200 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 201 */     if (!event.isCanceled()) {
/* 202 */       this.value = this.plannedValue;
/*     */     } else {
/* 204 */       this.plannedValue = this.value;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void increaseEnumNoEvent() {
/* 210 */     this.value = (T)EnumConverter.increaseEnum((Enum)this.value);
/*     */   }
/*     */   
/*     */   public String getType() {
/* 214 */     if (isEnumSetting()) {
/* 215 */       return "Enum";
/*     */     }
/* 217 */     return getClassName(this.defaultValue);
/*     */   }
/*     */   
/*     */   public <T> String getClassName(T value) {
/* 221 */     return value.getClass().getSimpleName();
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 225 */     if (this.description == null) {
/* 226 */       return "";
/*     */     }
/* 228 */     return this.description;
/*     */   }
/*     */   
/*     */   public boolean isNumberSetting() {
/* 232 */     return (this.value instanceof Double || this.value instanceof Integer || this.value instanceof Short || this.value instanceof Long || this.value instanceof Float);
/*     */   }
/*     */   
/*     */   public boolean isEnumSetting() {
/* 236 */     return (!isNumberSetting() && !(this.value instanceof String) && !(this.value instanceof Bind) && !(this.value instanceof Character) && !(this.value instanceof Boolean));
/*     */   }
/*     */   
/*     */   public boolean isStringSetting() {
/* 240 */     return this.value instanceof String;
/*     */   }
/*     */   
/*     */   public T getDefaultValue() {
/* 244 */     return this.defaultValue;
/*     */   }
/*     */   
/*     */   public String getValueAsString() {
/* 248 */     return this.value.toString();
/*     */   }
/*     */   
/*     */   public boolean hasRestriction() {
/* 252 */     return this.hasRestriction;
/*     */   }
/*     */   
/*     */   public void setVisibility(Predicate<T> visibility) {
/* 256 */     this.visibility = visibility;
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 260 */     if (this.visibility == null) {
/* 261 */       return true;
/*     */     }
/* 263 */     return this.visibility.test(getValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\setting\Setting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */