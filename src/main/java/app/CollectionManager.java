package app;

import commands.*;
import modules.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

public class CollectionManager {
    private Queue<Movie> movies = new PriorityQueue<>();
    private Integer nextId = 1;
    private static final String FILE_NAME = "src/main/resources/collection.csv";
    private LocalDateTime initializationDate;
    private Set<String> executedScripts = new HashSet<>();

    public CollectionManager() {
        this.initializationDate = LocalDateTime.now();
        load();
    }

    public String add(Movie movie) {
        movie.setId(getNextId());
        movies.add(movie);
        return "Movie added successfully.";
    }

    public Integer getNextId() {
        return nextId++;
    }

    public String remove(Movie movie) {
        if (movies.remove(movie)) {
            return "Movie removed successfully.";
        } else {
            return "Movie not found.";
        }
    }

    public String show() {
        if (movies.isEmpty()) {
            return "Collection is empty.";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Movie movie : movies) {
                sb.append(movie.toString()).append("\n");
            }
            return "Collection:\n" + sb.toString();
        }
    }

    public String removeById(int id) {
        for (Movie movie : movies) {
            if (movie.getId() == id) {
                movies.remove(movie);
                return "Movie with id " + id + " removed successfully.";
            }
        }
        return "Movie with id " + id + " not found.";
    }

    public String clear() {
        movies.clear();
        return "Collection cleared successfully.";
    }

    public String addIfMin(Movie movie) {
        if (movies.isEmpty() || movie.compareTo(movies.peek()) < 0) {
            movie.setId(getNextId());
            movies.add(movie);
            return "Movie added successfully.";
        } else {
            return "Movie is not the smallest. Not added.";
        }
    }

    public String removeGreater(Movie movie) {
        int removedCount = 0;
        for (Movie m : new PriorityQueue<>(movies)) {
            if (m.compareTo(movie) > 0) {
                movies.remove(m);
                removedCount++;
            }
        }
        return "Removed " + removedCount + " movies greater than the specified movie.";
    }


    public String minByCoordinates() {
        if (movies.isEmpty()) {
            return "Collection is empty";
        }

        Movie minMovie = movies.peek();
        for (Movie movie : movies) {
            if ((movie.getCoordinates().compareTo(minMovie.getCoordinates())) < 0) {
                minMovie = movie;
            }
        }
        return minMovie.toString();
    }

    public String maxById() {
        if (movies.isEmpty()) {
            return "Collection is empty.";
        } else {
            Movie maxMovie = movies.stream()
                    .max((m1, m2) -> Integer.compare(m1.getId(), m2.getId()))
                    .orElse(null);
            return maxMovie != null ? maxMovie.toString() : "No movie found.";
        }
    }

    public String getInfo() {
        return "Collection type: " + movies.getClass().getName() + "\n" +
                "Initialization date: " + initializationDate + "\n" +
                "Number of elements: " + movies.size();
    }

    public String executeScript(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                String[] commandParts = line.split(" ", 2);
                String commandName = commandParts[0];
                String commandArgs = commandParts.length > 1 ? commandParts[1] : "";

                Command command = null;
                switch (commandName) {
                    case "add":
                        Movie movie = MovieFactory.createMovie();
                        command = new AddCommand(new AddCommandArgs(movie));
                        break;
                    case "show":
                        command = new ShowCommand();
                        break;
                    case "remove_by_id":
                        try {
                            int id = Integer.parseInt(commandArgs);
                            command = new RemoveByIdCommand(id);
                        } catch (NumberFormatException e) {
                            result.append("Invalid ID format. Please enter a valid number.\n");
                            continue;
                        }
                        break;
                    case "update":
                        try {
                            String[] updateParts = commandArgs.split(" ", 2);
                            int updateId = Integer.parseInt(updateParts[0]);
                            Movie updatedMovie = MovieFactory.createMovie();
                            command = new UpdateCommand(updateId, updatedMovie);
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            result.append("Invalid command format. Please use 'update id'.\n");
                            continue;
                        }
                        break;
                    case "remove_head":
                        command = new RemoveHeadCommand();
                        break;
                    case "clear":
                        command = new ClearCommand();
                        break;
                    case "execute_script":
                        command = new ExecuteScriptCommand(commandArgs);
                        break;
                    case "count_greater_than_genre":
                        try {
                            MovieGenre genre = MovieGenre.valueOf(commandArgs.toUpperCase());
                            command = new CountGreaterThanGenreCommand(genre);
                        } catch (IllegalArgumentException e) {
                            result.append("Invalid genre. Please enter a valid genre.\n");
                            continue;
                        }
                        break;
                    case "add_if_min":
                        Movie newMovie = MovieFactory.createMovie();
                        command = new AddIfMinCommand(newMovie);
                        break;
                    case "remove_greater":
                        Movie greaterMovie = MovieFactory.createMovie();
                        command = new RemoveGreaterCommand(greaterMovie);
                        break;
                    case "min_by_coordinates":
                        command = new MinByCoordinatesCommand();
                        break;
                    case "max_by_id":
                        command = new MaxByIdCommand();
                        break;
                    case "help":
                        command = new HelpCommand();
                        break;
                    case "exit":
                        command = new ExitCommand();
                        break;
                    default:
                        result.append("Unknown command.\n");
                        continue;
                }

                if (command != null) {
                    result.append(command.execute(this, null)).append("\n");
                }
            }
            return result.toString();
        } catch (IOException e) {
            return "Error executing script: " + e.getMessage();
        }
    }

    public boolean checkIdExists(int id) {
        for (Movie movie : movies) {
            if (movie.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public String save() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.append("id,name,coordinates_x,coordinates_y,creationDate,oscarsCount,length,genre,mpaaRating,director_name,director_passportID,director_location_x,director_location_y,director_location_name\n");
            for (Movie movie : movies) {
                writer.append(movieToCsv(movie)).append("\n");
            }
            return "Collection saved successfully.";
        } catch (IOException e) {
            return "Error saving collection: " + e.getMessage();
        }
    }

    private String movieToCsv(Movie movie) {
        return movie.getId() + "," +
                movie.getName() + "," +
                movie.getCoordinates().getX() + "," +
                movie.getCoordinates().getY() + "," +
                movie.getCreationDate() + "," +
                movie.getOscarsCount() + "," +
                movie.getLength() + "," +
                movie.getGenre() + "," +
                movie.getMpaaRating() + ","+
                movie.getDirector().getName() + "," +
                movie.getDirector().getPassportID() + "," +
                movie.getDirector().getLocation().getX() + "," +
                movie.getDirector().getLocation().getY() + "," +
                movie.getDirector().getLocation().getName();
    }

    public String update(int id, Movie updatedMovie) {
        for (Movie movie : movies) {
            if (movie.getId() == id) {
                updatedMovie.setId(id);
                movies.remove(movie);
                movies.add(updatedMovie);
                return "Movie with id " + id + " updated successfully.";
            }
        }
        return "Movie with id " + id + " not found.";
    }

    public String removeHead() {
        if (movies.isEmpty()) {
            return "Collection is empty.";
        } else {
            Movie firstMovie = movies.poll();
            return "First movie removed successfully: " + firstMovie.toString();
        }
    }

    public String countGreaterThanGenre(MovieGenre genre) {
        int count = 0;
        for (Movie movie : movies) {
            if (movie.getGenre() != null && movie.getGenre().compareTo(genre) > 0) {
                count++;
            }
        }
        return "Number of movies with genre greater than " + genre + ": " + count;
    }

    public Queue<Movie> getMovies() {
        return movies;
    }

    private void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("File not found. Starting with an empty collection.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Пропускаем заголовок
                }
                Movie movie = csvToMovie(line);
                if (movie != null) {
                    movies.add(movie);
                    if (movie.getId() >= nextId) {
                        nextId = movie.getId() + 1;
                    }
                }
            }
            System.out.println("Collection loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading collection: " + e.getMessage());
        }
    }

    private Movie csvToMovie(String csvLine) {
        String[] values = csvLine.split(",");
        try {
            Movie movie= new Movie();
            movie.setId((Integer.parseInt(values[0])));
            movie.setName(values[1]);

            double coordinatesX = Double.parseDouble(values[2]);
            long coordinatesY = Long.parseLong(values[3]);
            movie.setCoordinates(new Coordinates(coordinatesX, coordinatesY));

            movie.setCreationDate(LocalDateTime.parse(values[4]));
            movie.setOscarsCount(Integer.parseInt(values[5]));
            movie.setLength(Integer.parseInt(values[6]));

            try{movie.setGenre(MovieGenre.valueOf(values[7]));} catch (Exception e){};
            try{movie.setMpaaRating(MpaaRating.valueOf(values[8]));} catch (Exception e){};

            String locationName;
            if (values.length < 14) {
                locationName = "";
            } else {
                locationName = values[13];
            }

            movie.setDirector(new Person(values[9], values[10], new Location(Long.parseLong(values[11]),
                    Double.parseDouble(values[12]), locationName)));

            return movie;
        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing movie from CSV: " + e.getMessage());
            return null;
        }
    }
}
