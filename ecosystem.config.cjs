module.exports = {
  apps: [
    {
      name: 'tellevo-admin',
      script: 'server.js',
      // Optional: set the working directory on the server when running `pm2 start`
      // cwd: '/var/www/administracion',
      interpreter: 'bun',
      env: {
        NODE_ENV: 'production',
        PORT: process.env.PORT || '3000'
      },
      instances: 1,
      exec_mode: 'fork',
      max_memory_restart: '300M',
      merge_logs: true
      // Optionally set log files (ensure directories exist)
      // error_file: '/var/log/pm2/tellevo-admin.err.log',
      // out_file: '/var/log/pm2/tellevo-admin.out.log'
    }
  ]
}
