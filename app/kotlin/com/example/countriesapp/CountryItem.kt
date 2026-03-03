// C:/Users/LENOVO/Desktop/Byishimo/CountriesApp/app/src/main/java/com/example/countriesapp/ui/home/HomeScreen.kt

// ... existing imports ...
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.Public
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.glance.layout.size
import androidx.glance.layout.width

// ... (keep HomeUiState and HomeScreen top part)

@androidx.compose.runtime.Composable
fun CountryItem(country: CountrySummary, onClick: () -> Unit) {
    androidx.compose.material3.Card(
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = androidx.compose.ui.Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            // Enhanced Image loading with Placeholder and Error handling
            coil.compose.AsyncImage(
                model = country.flags.png,
                contentDescription = "Flag of ${country.name.common}",
                modifier = androidx.compose.ui.Modifier
                    .size(width = 80.dp, height = 50.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                placeholder = rememberVectorPainter(Icons.Default.Public),
                error = rememberVectorPainter(Icons.Default.Public)
            )

            androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.width(16.dp))

            androidx.compose.foundation.layout.Column {
                androidx.compose.material3.Text(
                    text = country.name.common,
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                androidx.compose.material3.Text(
                    text = "Population: ${formatPopulation(country.population)}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}