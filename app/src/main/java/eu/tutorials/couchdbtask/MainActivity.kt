package eu.tutorials.couchdbtask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.DataSource
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import com.couchbase.lite.DatabaseConfigurationFactory
import com.couchbase.lite.Expression
import com.couchbase.lite.MutableArray
import com.couchbase.lite.MutableDictionary
import com.couchbase.lite.MutableDocument
import com.couchbase.lite.QueryBuilder
import com.couchbase.lite.SelectResult
import com.couchbase.lite.newConfig


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CouchbaseLite.init(this)


        val database = Database("getting-started", DatabaseConfigurationFactory.newConfig())
        var collection = database.createCollection("Verlaine")
       // both of these retrieve collection1 created above
        collection = database.getCollection("Verlaine")!!
        collection = database.defaultScope.getCollection("Verlaine")!!
//        val collection = database.getCollection("myCollection")
//            ?: throw IllegalStateException("collection not found")



        val mutableDoc = MutableDocument()

        val address = MutableDictionary()
        address.setString("street", "1 Main st.")
        address.setString("city", "San Francisco")
        address.setString("state", "CA")
        address.setString("country", "USA")
        address.setString("code", "90210")

        // Create and populate mutable array
        val phones = MutableArray()
        phones.addString("650-000-0000")
        phones.addString("650-000-0001")


        // Initialize and populate the document
        mutableDoc.setString("type", "hotel")
        mutableDoc.setString("name", "Hotel Java Mo")
        mutableDoc.setFloat("room_rate", 121.75f)
        mutableDoc.setDictionary("address", address)
        mutableDoc.setArray("phones", phones)


        collection.save(mutableDoc)


        val queryAll = QueryBuilder
            .select(SelectResult.all())
            .from(DataSource.collection(collection))
            .where(Expression.property("type").equalTo(Expression.string("hotel")))

        //Result set Access

        queryAll.execute().use { rs ->
            rs.allResults().forEach {
                Log.d("TAG", "Hotel name -> ${it.getString("name")}, in ${it.getString("country")}")
            }
        }
    }
}
