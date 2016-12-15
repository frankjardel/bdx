import os
import sys
import bpy
import subprocess
from .. import utils as ut


class BdxExpRun(bpy.types.Operator):
    """Export scenes to .bdx files, and run the BDX simulation"""
    bl_idname = "object.bdxexprun"
    bl_label = "Export and Run"

    def execute(self, context):

        # Set the mouse cursor to "WAIT" as soon as exporting starts
        context.window.cursor_set("WAIT")

        j = os.path.join

        proot = ut.project_root()
        sroot = ut.src_root()
        asset_dir = j(proot, "android", "assets", "bdx")
        prof_scene_name = "__Profiler"
        bdx_scenes_dir = j(asset_dir, "scenes")

        # Delete old scene files except for the profiler.
        if os.path.isdir(bdx_scenes_dir):
            old_scenes = ut.listdir(bdx_scenes_dir)
            for f in old_scenes:
                if os.path.basename(f) != prof_scene_name + ".bdx":
                    os.remove(f)

        # Check if profiler scene needs export:
        prof_scene_export = context.scene.game_settings.show_framerate_profile and prof_scene_name + ".bdx" not in os.listdir(bdx_scenes_dir)

        if prof_scene_export:
        
            # Append profiler scene from default blend file:
            prof_blend_name = "profiler.blend"
            prof_scene_path = j(prof_blend_name, "Scene", prof_scene_name)
            prof_scene_dir = j(ut.gen_root(), prof_blend_name, "Scene", "")

            bpy.ops.wm.append(filepath=prof_scene_path, directory=prof_scene_dir, filename=prof_scene_name)

        # Save-out internal java files
        saved_out_files = ut.save_internal_java_files(sroot)

        # Clear inst dir (files generated by export_scene)
        inst = j(ut.src_root(), "inst")
        if os.path.isdir(inst):
            inst_files = ut.listdir(inst)
            for f in inst_files:
                os.remove(f)
        else:
            os.mkdir(inst)

        # Export scenes:
        for i in range(len(bpy.data.scenes)):
            scene = bpy.data.scenes[i]
            file_name =  scene.name + ".bdx"
            file_path = j(asset_dir, "scenes", file_name)
            sys.stdout.write("\rBDX - Exporting Scene: {0} ({1}/{2})                            ".format(scene.name, i+1, len(bpy.data.scenes)))
            sys.stdout.flush()
            bpy.ops.export_scene.bdx(filepath=file_path, scene_name=scene.name, exprun=True)

        print("")       ## Added blank line for reading comfort

        if prof_scene_export:
        
            # Remove temporal profiler scene:
            version = float("{}.{}".format(*bpy.app.version))
            if version >= 2.78:
                bpy.data.scenes.remove(bpy.data.scenes[prof_scene_name], True)
            else:
                bpy.data.scenes.remove(bpy.data.scenes[prof_scene_name])

        # Modify relevant files:
        bdx_app = j(sroot, "BdxApp.java")

        # - BdxApp.java
        new_lines = []
        for scene in bpy.data.scenes:
            class_name = ut.str_to_valid_java_class_name(scene.name)
            if os.path.isfile(j(sroot, "inst", class_name + ".java")):
                inst = "new " + ut.package_name() + ".inst." + class_name + "()"
            else:
                inst = "null"

            new_lines.append('("{}", {});'.format(scene.name, inst))


        put = "\t\tScene.instantiators.put"

        ut.remove_lines_containing(bdx_app, put)

        ut.insert_lines_after(bdx_app, "Scene.instantiators =", [put + l for l in new_lines])

        scene = bpy.context.scene
        ut.replace_line_containing(bdx_app, "scenes.add", '\t\tBdx.scenes.add(new Scene("'+scene.name+'"));');

        ut.remove_lines_containing(bdx_app, "Bdx.firstScene = ")
        ut.insert_lines_after(bdx_app, "scenes.add", ['\t\tBdx.firstScene = "'+scene.name+'";'])

        # - DesktopLauncher.java
        rx = str(scene.render.resolution_x)
        ry = str(scene.render.resolution_y)

        dl = j(ut.src_root("desktop", "DesktopLauncher.java"), "DesktopLauncher.java")
        ut.set_file_var(dl, "title", '"'+ut.project_name()+'"')
        ut.set_file_var(dl, "width", rx)
        ut.set_file_var(dl, "height", ry)

        # - AndroidLauncher.java
        al = j(ut.src_root("android", "AndroidLauncher.java"), "AndroidLauncher.java")
        ut.set_file_var(al, "width", rx)
        ut.set_file_var(al, "height", ry)

        # Run engine:

        gradlew = "gradlew"
        if os.name != "posix":
            gradlew += ".bat"
        
        print(" ")
        print("------------ BDX START --------------------------------------------------")
        print(" ")
        try:
            subprocess.check_call([os.path.join(proot, gradlew), "-p", proot, "desktop:run"])
        except subprocess.CalledProcessError:
            self.report({"ERROR"}, "BDX BUILD FAILED")
        print(" ")
        print("------------ BDX END ----------------------------------------------------")
        print(" ")

        # Delete previously saved-out internal files
        for fp in saved_out_files:
            os.remove(fp)

        context.window.cursor_set("DEFAULT")
        
        return {'FINISHED'}


def register():
    bpy.utils.register_class(BdxExpRun)


def unregister():
    bpy.utils.unregister_class(BdxExpRun)
