package org.spongepowered.asm.mixin.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Slice {
  String id() default "";
  
  At from() default @At("HEAD");
  
  At to() default @At("TAIL");
}


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\org\spongepowered\asm\mixin\injection\Slice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */