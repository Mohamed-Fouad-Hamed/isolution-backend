param(
    [Parameter(Mandatory=$true)]
    [ValidateSet("up", "down", "restart", "build")]
    [string]$action
)

$composeFile = "docker-compose.dev.yml"

switch ($action) {
    "up" {
        Write-Host "Starting development environment..."
        docker compose -f $composeFile up --build -d
    }

    "down" {
        Write-Host "Stopping and removing all containers..."
        docker compose -f $composeFile down -v
    }

    "restart" {
        Write-Host "Restarting containers..."
        docker compose -f $composeFile down -v
        docker compose -f $composeFile up --build -d
    }

    "build" {
        Write-Host "Rebuilding all services without starting..."
        docker compose -f $composeFile build --no-cache
    }
}
