import java.util.Scanner;

public class MovieFactory {

    private static final Scanner scanner = new Scanner(System.in);

    public static Movie createMovie() {
        Movie movie = new Movie();

        movie.setName(readName("movie"));
        movie.setCoordinates(new Coordinates(readCoordinateX(), readCoordinateY()));
        movie.setOscarsCount(readOscarsCount());
        movie.setLength(readLength());
        movie.setGenre(readGenre());
        movie.setMpaaRating(readMpaaRating());
        movie.setDirector(new Person(readName("director"), readPassportId(), new Location(readLocX(), readLocY(), scanner.nextLine().trim())));

        return movie;
    }

    private static String readName(String nameOf) {
        while (true) {
            if (nameOf.equals("movie")) {
                System.out.print("Enter movie name: ");
            } else {
                System.out.print("Enter director name: ");
            }

            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                return name;
            } else {
                System.out.println("Name cannot be empty. Please try again.");
            }
        }
    }


    private static Double readCoordinateX() {
        while (true) {
            try {
                System.out.print("Enter coordinate X: ");
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid format. Please enter coordinates as 'x'.");
            }
        }
    }

    private static long readCoordinateY() {
        while (true) {
            try {
                System.out.print("Enter coordinate Y: ");
                long y = Long.parseLong(scanner.nextLine().trim());
                if (y <= -177) {
                    System.out.println("Coordinate Y must be greater than -177, please try again.");
                } else {
                    return y;
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid format. Please enter coordinates as 'y'.");
            }
        }
    }

    private static int readOscarsCount() {
        while (true) {
            try {
                System.out.print("Enter oscars count: ");
                int oscarsCount = Integer.parseInt(scanner.nextLine().trim());
                if (oscarsCount > 0) {
                    return oscarsCount;
                } else {
                    System.out.println("Oscars count must be greater than 0. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid format. Please enter a valid number.");
            }
        }
    }

    private static Integer readLength() {
        while (true) {
            try {
                System.out.print("Enter length: ");
                int length = Integer.parseInt(scanner.nextLine().trim());
                if (length > 0) {
                    return length;
                } else {
                    System.out.println("Length must be greater than 0. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid format. Please enter a valid number.");
            }
        }
    }

    private static MovieGenre readGenre() {
        while (true) {
            System.out.print("Enter genre (DRAMA, COMEDY, ADVENTURE, HORROR, FANTASY) or leave empty: ");
            String genreInput = scanner.nextLine().trim().toUpperCase();
            if (genreInput.isEmpty()) {
                return null;
            }
            try {
                return MovieGenre.valueOf(genreInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid genre. Please try again.");
            }
        }
    }

    private static MpaaRating readMpaaRating() {
        while (true) {
            System.out.print("Enter MPAA rating (G, R, NC_17) or leave empty: ");
            String mpaaRatingInput = scanner.nextLine().trim().toUpperCase();
            if (mpaaRatingInput.isEmpty()) {
                return null;
            }
            try {
                return MpaaRating.valueOf(mpaaRatingInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid MPAA rating. Please try again.");
            }
        }
    }

    public static String readPassportId() {
        while (true) {
            System.out.print("Enter passport ID: ");
            String passportID = scanner.nextLine().trim();
            if (passportID.isEmpty()) {
                System.out.println("Passport ID cannot be empty. Please try again.");
            } else {
                return passportID;
            }
        }
    }

    public static long readLocX() {
        while (true) {
            try {
                System.out.print("Enter location X: ");
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid format. Please enter a valid number.");
            }
        }
    }

    public static double readLocY() {
        while (true) {
            try {
                System.out.print("Enter location Y: ");
                double y = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("Enter location name: ");
                return y;
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid format. Please enter a valid number.");
            }
        }
    }
}