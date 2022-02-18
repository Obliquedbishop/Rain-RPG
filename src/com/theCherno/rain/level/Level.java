package com.theCherno.rain.level;

import com.theCherno.rain.entity.Entity;
import com.theCherno.rain.entity.mob.Mob;
import com.theCherno.rain.entity.mob.Player;
import com.theCherno.rain.entity.particle.Particle;
import com.theCherno.rain.entity.projectile.Projectile;
import com.theCherno.rain.events.Event;
import com.theCherno.rain.graphics.Screen;
import com.theCherno.rain.layers.Layer;
import com.theCherno.rain.level.tile.Tile;
import com.theCherno.rain.util.Vector2i;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Level extends Layer {

    protected int width, height;
    protected int[] tilesInt;
    protected int[] tiles; // Contain the colour value of every pixel in that level

    private int xScroll, yScroll;
    private int c;
    private final List<Entity> entities = new ArrayList<Entity>();
    private final List<Projectile> projectiles = new ArrayList<Projectile>();
    private final List<Particle> particles = new ArrayList<Particle>();

    private final List<Player> players = new ArrayList<Player>();

    private final Comparator<Node> nodeSorter = new Comparator<Node>() {
        public int compare(Node n0, Node n1) {
            return Double.compare(n0.fCost, n1.fCost);
        }
    };

    public static Level spawn = new SpawnLevel("/level/spawn.png");

    /**
     * Constructor to create a random level
     */
    public Level(int width, int height) {
        this.width = width;
        this.height = height;
        tilesInt = new int[width * height];
        generateLevel();
    }

    /**
     *Constructor to load a level
     */

    public Level(String path) {
        loadLevel(path);
        generateLevel();
    }

    protected void generateLevel() {
    }

    protected void loadLevel(String path) {
    }

    public void onEvent(Event event) {
        getClientPlayer().onEvent(event);
    }

    public void update() {
        for (Entity entity : entities) {
            entity.update();
        }

        for (Projectile projectile : projectiles) {
            projectile.update();
        }

        for(Particle particle: particles) {
            particle.update();
        }

        for(Player player: players) {
            player.update();
        }
        remove();
    }

    private void remove() {
        entities.removeIf(Entity::isRemoved);

        projectiles.removeIf(Projectile::isRemoved);

        particles.removeIf(Particle::isRemoved);

        players.removeIf(Player::isRemoved);
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    private void time() {
    }

    public boolean tileCollision(int x, int y, int size, int xOffset, int yOffset) {
        boolean solid = false;
        for(c = 0; c < 4; c++) {
            int xt = (x + c % 2 * size + xOffset) >> 4;
            int yt = (y + c / 2 * size + yOffset) >> 4;
            if(getTile(xt, yt).solid()) {
                solid = true;
                break;
            }
        }
        return solid;
    }

    public int CollisionPoint() {
        return c;
    }

    public void setScroll(int xScroll, int yScroll) {
        this.xScroll = xScroll;
        this.yScroll = yScroll;
    }

    public void render(Screen screen) {
        screen.setOffset(xScroll, yScroll);
        int x0 = xScroll >> 4;
        int x1 = (xScroll + screen.width + 16) >> 4;
        int y0 = yScroll >> 4;
        int y1 = (yScroll + screen.height + 16) >> 4;

        for(int y = y0; y < y1; y++) {
            for(int x = x0; x < x1; x++) {
                getTile(x, y).render(x, y, screen);
            }
        }
        for (Entity entity : entities) {
            entity.render(screen);
        }
        for (Projectile projectile : projectiles) {
            projectile.render(screen);
        }
        for(Particle particle: particles) {
            particle.render(screen);
        }
        for(Player player: players) {
            player.render(screen);
        }
    }

    public void add(Entity e) {
        e.init(this);
        if(e instanceof Particle) {
            particles.add((Particle) e);
        } else if(e instanceof Projectile) {
            projectiles.add((Projectile) e);
        } else if(e instanceof Player) {
            players.add((Player) e);
        } else {
            entities.add(e);
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayerAt(int index) {
        return players.get(index);
    }

    public Player getClientPlayer() {
        return players.get(0);
    }

    public List<Node> findPath(Vector2i start, Vector2i goal) {
        List<Node> openList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();
        Node current = new Node(start, null, 0, getDistance(start, goal));
        openList.add(current);
        while(openList.size() > 0) {
            openList.sort(nodeSorter);
            current = openList.get(0);
            if (current.tile.equals(goal)) {
                List<Node> path = new ArrayList<Node>();
                while(current.parent != null) {
                    path.add(current);
                    current = current.parent;
                }
                openList.clear();
                closedList.clear();
                return path;
            }
            openList.remove(current);
            closedList.add(current);
            for(int i = 0; i < 9; i++) {
                if(i == 4) continue;
                int x = current.tile.getX();
                int y = current.tile.getY();
                int xi = (i % 3) - 1;
                int yi = (i / 3) - 1;
                Tile at = getTile(x + xi, y + yi);
                if(at == null) continue;
                if(at == Tile.voidTile) continue;
                if(at.solid()) continue;
                Vector2i a = new Vector2i(x + xi, y + yi);
                double gCost = current.gCost + (getDistance(current.tile, a) == 1 ? 1 : 0.95);
                double hCost = getDistance(a, goal);
                Node node = new Node(a, current, gCost, hCost);
                if (vecInList(closedList, a) && gCost >= node.gCost) continue;
                if(!vecInList(openList, a) || gCost < node.gCost) openList.add(node);
            }
        }
        closedList.clear();
        return null;
    }

    private boolean vecInList(List<Node> list, Vector2i vector) {
        for(Node n : list) {
            if(n.tile.equals(vector)) return true;
        }
        return false;
    }

    private double getDistance(Vector2i tile, Vector2i goal) {
        double dx = tile.getX() - goal.getX();
        double dy = tile.getY() - goal.getY();
        double distance = Math.sqrt((dx * dx) + (dy * dy));
        return distance;
    }

    public List<Entity> getEntities(Entity e, int radius) {
        List<Entity> result = new ArrayList<Entity>();
        int ex = (int) e.getX();
        int ey = (int) e.getY();
        for (Entity entity : entities) {
            if(entity.equals(e)) continue;
            if(!(entity instanceof Mob)) continue;
            int x = (int) entity.getX();
            int y = (int) entity.getY();

            int dx = Math.abs(x - ex);
            int dy = Math.abs(y - ey);
            double distance = Math.sqrt((dx * dx) + (dy * dy));
            if (distance <= radius) result.add(entity);
        }
        return result;
    }

    public List<Player> getPlayers(Entity e, int radius) {
        List<Player> result = new ArrayList<Player>();
        int ex = (int) e.getX();
        int ey = (int) e.getY();
        for (Player player : players) {
            int x = (int) player.getX();
            int y = (int) player.getY();

            int dx = Math.abs(x - ex);
            int dy = Math.abs(y - ey);
            double distance = Math.sqrt((dx * dx) + (dy * dy));
            if (distance <= radius) result.add(player);
        }
        return result;
    }


    // Grass = 0xFF00FF00
    // Flower = 0xFFFFFF00
    // Rock = 0xFF7F7F00
    public Tile getTile(int x, int y) {
        if(x < 0 || y < 0 || x >= width || y >= height) return Tile.voidTile;
        if(tiles[x + y * width] == Tile.col_spawn_floor) return Tile.spawn_floor;
        if(tiles[x + y * width] == Tile.col_spawn_wall1) return Tile.spawn_wall1;
        if(tiles[x + y * width] == Tile.col_spawn_grass) return Tile.spawn_grass;
        if(tiles[x + y * width] == Tile.col_spawn_wall2) return Tile.spawn_wall2;
        if(tiles[x + y * width] == Tile.col_spawn_water) return Tile.spawn_water;
        if(tiles[x + y * width] == Tile.col_spawn_hedge) return Tile.spawn_hedge;
        return Tile.voidTile;
    }


}
