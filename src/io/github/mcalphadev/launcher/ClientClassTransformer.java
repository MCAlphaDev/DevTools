package io.github.mcalphadev.launcher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClientClassTransformer implements IClassTransformer {
	final Map<String, ClassTransform> brokenAccess = new HashMap<>();

	public ClientClassTransformer() {
		// Used in cz
		publicField("net.minecraft.util.BlockPos", "f");
		publicField("net.minecraft.util.BlockPos", "g");
		publicField("net.minecraft.util.BlockPos", "h");
		publicField("net.minecraft.util.BlockPos", "i");

		// Used in fx
		publicField("net.minecraft.util.BlockPos", "e");

		// Used in unmapped recipe classes
		publicMethod("net.minecraft.game.recipe.ShapedRecipes", "addRecipe(Lnet/minecraft/game/item/ItemInstance;[Ljava/lang/Object;)V");

		// used in ShapedRecipes
		publicClass("fs");
		publicMethod("fs", "<init>(Lnet/minecraft/game/recipe/ShapedRecipes;)V");

		// used in mm
		publicField("net.minecraft.gui.Gui", "a");
		publicField("net.minecraft.gui.Gui", "h");

		// used in error screen
		publicClass("db");
		publicClass("nb");
	}

	public void publicClass(String name) {
		brokenAccess.computeIfAbsent(name.replace('/', '.'), k -> new ClassTransform()).classToo = true;
	}

	public void publicMethod(String owner, String descriptor) {
		brokenAccess.computeIfAbsent(owner.replace('/', '.'), k -> new ClassTransform()).methods.add(descriptor);
	}

	public void publicField(String owner, String name) {
		brokenAccess.computeIfAbsent(owner.replace('/', '.'), k -> new ClassTransform()).fields.add(name);
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] byteCode) {
		if (brokenAccess.containsKey(name)) {
			ClassReader reader = new ClassReader(byteCode);
			ClassWriter writer = new ClassWriter(reader, 0);

			ClassTransform transformation = brokenAccess.get(transformedName);
			reader.accept(new ClassVisitor(Opcodes.ASM7, writer) {
				private int transform(int access) {
					return (access & ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED)) | Opcodes.ACC_PUBLIC;
				}

				@Override
				public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
					super.visit(version, transformation.classToo ? transform(access) : access, name, signature, superName, interfaces);
				}

				@Override
				public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
					return super.visitMethod(transformation.methods.contains(name + descriptor) ? transform(access) : access, name, descriptor, signature, exceptions);
				}

				@Override
				public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
					return super.visitField(transformation.fields.contains(name) ? transform(access) : access, name, descriptor, signature, value);
				}
			}, 0);

			return writer.toByteArray();
		} else {
			return byteCode;
		}
	}

	private static class ClassTransform {
		boolean classToo = false;
		final Set<String> fields = new HashSet<>();
		final Set<String> methods = new HashSet<>();
	}
}
