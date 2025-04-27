import java.util.Scanner;

public class MovieFactory {

    private static final Scanner scanner = new Scanner(System.in);

    public static Movie createMovie() {
        Movie movie = new Movie();

        System.out.print("Enter movie name: ");
        movie.setName(scanner.nextLine());

        System.out.print("Enter coordinates (x y): ");
        String[] coordinatesInput = scanner.nextLine().split(" ");
        Double x = Double.parseDouble(coordinatesInput[0]);
        long y = Long.parseLong(coordinatesInput[1]);
        movie.setCoordinates(new Coordinates(x, y));

        System.out.print("Enter oscars count: ");
        movie.setOscarsCount(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter length (or leave empty): ");
        String lengthInput = scanner.nextLine();
        if (!lengthInput.isEmpty()) {
            movie.setLength(Integer.parseInt(lengthInput));
        }

        System.out.print("Enter genre (ACTION, DRAMA, COMEDY, THRILLER, FANTASY, SCIENCE_FICTION) or leave empty: ");
        String genreInput = scanner.nextLine();
        if (!genreInput.isEmpty()) {
            movie.setGenre(MovieGenre.valueOf(genreInput.toUpperCase()));
        }

        System.out.print("Enter MPAA rating (G, PG, PG_13, R, NC_17) or leave empty: ");
        String mpaaRatingInput = scanner.nextLine();
        if (!mpaaRatingInput.isEmpty()) {
            movie.setMpaaRating(MpaaRating.valueOf(mpaaRatingInput.toUpperCase()));
        }

        System.out.print("Enter director name: ");
        String directorName = scanner.nextLine();

        System.out.print("Enter director passport ID: ");
        String passportID = scanner.nextLine();

        System.out.print("Enter director location (x y name) or leave empty: ");
        String locationInput = scanner.nextLine();
        Location location = null;
        if (!locationInput.isEmpty()) {
            String[] locationParts = locationInput.split(" ");
            long locX = Long.parseLong(locationParts[0]);
            double locY = Double.parseDouble(locationParts[1]);
            String locName = locationParts[2];
            location = new Location(locX, locY, locName);
        }

        movie.setDirector(new Person(directorName, passportID, location));

        return movie;
    }
}