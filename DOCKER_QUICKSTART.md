# Docker Quick Start Guide for MockForge

## Prerequisites

✅ Docker Desktop installed (or Docker Engine on Linux)
✅ At least 4GB RAM available
✅ 2GB free disk space

---

## Option 1: Build and Run with Docker Compose (Recommended)

### Step 1: Build the Image
```bash
docker compose build
```

This will:
- Build the backend with Maven
- Build the frontend with Node
- Create an optimized runtime image (~500MB)

### Step 2: Start MockForge
```bash
docker compose up -d
```

The `-d` flag runs it in detached mode (background).

### Step 3: Verify It's Running
```bash
# Check container status
docker compose ps

# Check health
curl http://localhost:8080/_mockforge/health

# View logs
docker compose logs -f
```

### Step 4: Open in Browser
```
http://localhost:8080
```

You should see the Phase 0 welcome screen!

---

## Option 2: Build Docker Image Directly

```bash
# Build the image
docker build -t mockforge:local .

# Run the container
docker run -d \
  --name mockforge \
  -p 127.0.0.1:8080:8080 \
  -v mockforge-data:/var/lib/mockforge \
  mockforge:local

# Check logs
docker logs -f mockforge

# Stop
docker stop mockforge

# Remove
docker rm mockforge
```

---

## Common Commands

### Start MockForge
```bash
docker compose up -d
```

### Stop MockForge
```bash
docker compose down
```

### Stop and Remove Data (⚠️ Destructive)
```bash
docker compose down -v
```

### View Logs
```bash
# All logs
docker compose logs

# Follow logs (Ctrl+C to exit)
docker compose logs -f

# Last 50 lines
docker compose logs --tail=50
```

### Restart MockForge
```bash
docker compose restart
```

### Rebuild After Code Changes
```bash
docker compose down
docker compose build
docker compose up -d
```

---

## Endpoints to Test

Once running, try these:

```bash
# Health check (should return {"status":"UP"})
curl http://localhost:8080/_mockforge/health

# Readiness check
curl http://localhost:8080/_mockforge/ready

# Diagnostics (system info)
curl http://localhost:8080/_mockforge/diagnostics

# Frontend (in browser)
open http://localhost:8080
```

---

## Troubleshooting

### Port 8080 Already in Use
If you get "port already allocated" error:

**Option 1: Change the port**
Edit `docker-compose.yml`:
```yaml
ports:
  - "127.0.0.1:8081:8080"  # Change 8080 to 8081
```

**Option 2: Stop the conflicting service**
```bash
# On Windows (find what's using port 8080)
netstat -ano | findstr :8080

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F
```

### Container Won't Start
```bash
# Check logs for errors
docker compose logs

# Check if image built successfully
docker images | grep mockforge

# Rebuild from scratch
docker compose down -v
docker compose build --no-cache
docker compose up -d
```

### Database Issues
```bash
# Remove volume and restart (⚠️ loses all data)
docker compose down -v
docker compose up -d
```

### Docker Desktop Not Running
Make sure Docker Desktop is running. You should see the Docker icon in your system tray.

---

## Data Persistence

### Where is Data Stored?
MockForge stores data in a Docker named volume: `mockforge-data`

```bash
# List volumes
docker volume ls | grep mockforge

# Inspect volume
docker volume inspect mockforge-data

# Backup data (while container is stopped)
docker compose down
docker run --rm -v mockforge-data:/data -v $(pwd):/backup alpine tar czf /backup/mockforge-backup.tar.gz -C /data .

# Restore data
docker run --rm -v mockforge-data:/data -v $(pwd):/backup alpine tar xzf /backup/mockforge-backup.tar.gz -C /data
```

---

## Development Workflow

### Making Code Changes

1. **Stop the container**
   ```bash
   docker compose down
   ```

2. **Make your code changes**

3. **Rebuild and start**
   ```bash
   docker compose build
   docker compose up -d
   ```

4. **Check logs**
   ```bash
   docker compose logs -f
   ```

---

## Performance Tips

### Build Faster
```bash
# Use BuildKit for faster builds
DOCKER_BUILDKIT=1 docker compose build
```

### Clean Up Old Images/Containers
```bash
# Remove unused images
docker image prune -a

# Remove stopped containers
docker container prune

# Remove unused volumes
docker volume prune

# Clean everything (⚠️ removes ALL unused Docker resources)
docker system prune -a
```

---

## Environment Variables

You can customize MockForge behavior with environment variables in `docker-compose.yml`:

```yaml
environment:
  - MOCKFORGE_PORT=8080           # Internal port
  - MOCKFORGE_LOG_LEVEL=INFO      # DEBUG, INFO, WARN, ERROR
  - MOCKFORGE_HOME=/var/lib/mockforge
  - MOCKFORGE_CORS_ORIGINS=http://localhost:3000
```

---

## Health Checks

Docker Compose includes automatic health checks:

```bash
# Check health status
docker compose ps

# Should show "healthy" after ~20 seconds
```

Health check command:
```bash
curl -f http://localhost:8080/_mockforge/health || exit 1
```

---

## Next Steps

Once MockForge is running:

1. ✅ Verify health endpoints work
2. ✅ See the Phase 0 welcome screen in browser
3. ⏳ Wait for Phase 1 to start creating projects and resources!

---

## Need Help?

- **Documentation**: Check `README.md` and `PHASE_0_SUMMARY.md`
- **Issues**: https://github.com/SUBHAM-RAJ-01/Mockforge/issues
- **Logs**: Always check logs first: `docker compose logs -f`

---

**Happy Mocking! 🚀**
