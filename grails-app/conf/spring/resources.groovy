import com.smartpaper.services.screen.ScreenService
import com.smartpaper.services.OpenCVService

// Place your Spring DSL code here
beans = {
   openCVService(OpenCVService) {
        burningImageService = ref('burningImageService')
   }

   screenService(ScreenService){
       firefoxDriversInstances = 1
       grailsApplication = ref('grailsApplication')
       burningImageService = ref('burningImageService')
   }
}