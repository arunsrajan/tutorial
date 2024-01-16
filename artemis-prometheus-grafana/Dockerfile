FROM apache/activemq-artemis:2.31.2
COPY artemis-prometheus-metrics-plugin-2.1.0.jar /opt/activemq-artemis/lib/
COPY metrics.war /opt/activemq-artemis/web/
COPY broker.xml /var/lib/artemis-instance/etc-override/
COPY bootstrap.xml /var/lib/artemis-instance/etc-override/
ENTRYPOINT ["/docker-run.sh"]
CMD ["run"]