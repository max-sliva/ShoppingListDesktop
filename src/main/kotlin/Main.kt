import com.dropbox.core.DbxException
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.FileMetadata
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

var client: DbxClientV2? = null

fun main(args: Array<String>) {
    println("Hello World!")
    val ACCESS_TOKEN = "sl.BDodKXliewvICFIS157lL_HmIiKsQwJG2lt5IPEMOeBt4RlUjdOuEh5wvSPtLyD3rXV6NMUz5snbPV8gGxFmMYozaxGupqRQY_JANpQB4DKxhzOyD4vUj5CvQf4ywe_LOKuzjFo"
    val config = DbxRequestConfig.newBuilder("dropbox/MyShoppingList3").build()
    client = DbxClientV2(config, ACCESS_TOKEN)
    val account = client!!.users().currentAccount
    println(account.name.displayName)
    var result = client!!.files().listFolder("")
    while (true) {
        for (metadata in result.entries) {
            println(metadata.pathLower)
            if (metadata.pathLower.contains("shoppinglist")) {
                println("Start download")
                downloadList(metadata.pathLower)
            }
        }
        if (!result.hasMore) {
            break
        }
        result = client!!.files().listFolderContinue(result.cursor)
    }
}

fun downloadList(path: String){
    try {
        //todo сделать загрузку в папку проекта без абсолютного пути
        val downloadFile: OutputStream = FileOutputStream("F:\\KtlnProjects\\untitled5\\shoppingList.txt")
        try {
            val metadata: FileMetadata = client!!.files().downloadBuilder(path)
                .download(downloadFile)
        } finally {
            downloadFile.close()
        }
    } //exception handled
    catch (e: DbxException) {
        //error downloading file
        //JOptionPane.showMessageDialog(null, "Unable to download file to local system\n Error: $e")
        println("Error: $e")
    } catch (e: IOException) {
        //error downloading file
        //JOptionPane.showMessageDialog(null, "Unable to download file to local system\n Error: $e")
        println("Error: $e")
    }
}