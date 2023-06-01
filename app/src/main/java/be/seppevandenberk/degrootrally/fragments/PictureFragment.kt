package be.seppevandenberk.degrootrally.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.model.ViewModelLoggedInUser
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.ktx.storage

//source = https://www.geeksforgeeks.org/android-upload-an-image-on-firebase-storage-with-kotlin/

class PictureFragment : Fragment() {
    private var storageRef = Firebase.storage.reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_picture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSelectPhotos: Button = view.findViewById(R.id.btnSelectPhotos)

        btnSelectPhotos.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerActivityResult.launch(intent)
        }
    }

    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
    // lambda expression to receive a result back, here we
        // receive single item(photo) on selection
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                // getting URI of selected Image
                val imageUri: Uri? = result.data?.data
                val sd = getFileName(requireContext(), imageUri!!)

                val user = ViewModelProvider(requireActivity())[ViewModelLoggedInUser::class.java]
                user.name.observe(viewLifecycleOwner) { name ->
                    val metadata = StorageMetadata.Builder().setContentType("image/jpeg")
                        .setCustomMetadata("key", name) // Add any custom metadata key-value pairs
                        .build()
                    val uploadTask = storageRef.child("file/$sd").putFile(imageUri, metadata)

                    uploadTask.addOnSuccessListener {
                        // using glide library to display the image
                        storageRef.child("file/$sd").downloadUrl.addOnSuccessListener {
                            Glide.with(this).load(it)
                                .into(requireActivity().findViewById(R.id.upload_image_vw))

                            Log.e("Firebase", "download passed")
                        }.addOnFailureListener {
                            Log.e("Firebase", "Failed in downloading")
                        }
                    }.addOnFailureListener {
                        Log.e("Firebase", "Image Upload fail")
                    }
                }
            }
        }

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }
}
