package ru.job4j.cars;

public class CarsRun {

    public static void main(String[] args) {
        /**
        List<Car> lastDayCars = new ArrayList<>();
        List<Car> carsWithPhotos = new ArrayList<>();
        List<Car> audiCars = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            AdRepository adRepository = new AdRepository(sf);
            lastDayCars = adRepository.getLastDayCarsAds();
            carsWithPhotos = adRepository.getCarsWithPhotoAds();
            audiCars = adRepository.getOnlyOneAd("Audi");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        System.out.println("Only for last day: " + lastDayCars.size());
        lastDayCars.forEach(System.out::println);
        System.out.println("Only with photo: " + carsWithPhotos.size());
        carsWithPhotos.forEach(System.out::println);
        System.out.println("Only Audi: " + audiCars.size());
        audiCars.forEach(System.out::println);
         */
    }
}
